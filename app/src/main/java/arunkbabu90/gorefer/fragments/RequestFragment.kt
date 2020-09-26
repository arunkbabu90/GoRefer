package arunkbabu90.gorefer.fragments

import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arunkbabu90.gorefer.EndlessRecyclerViewScrollListener
import arunkbabu90.gorefer.R
import arunkbabu90.gorefer.activities.MainActivity
import arunkbabu90.gorefer.adapter.PostAdapter
import arunkbabu90.gorefer.model.Post
import arunkbabu90.gorefer.network.RetrofitInterface
import kotlinx.android.synthetic.main.fragment_request.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestsFragment : Fragment() {
    private lateinit var postAdapter: PostAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var postsList: ArrayList<Post> = ArrayList()
    private var isDataLoaded = false
    private var cPageNo: Int = 1
    private val MAX_PAGES = 10

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerNetworkChangeCallback()

        postAdapter = PostAdapter(postsList) { post: Post ->
            // Item Click listener
            val MAX_CHARS = 40
            val title = post.title
            val endIndex = if (title.length < MAX_CHARS) title.length - 1 else MAX_CHARS
            val ellipsis = if (endIndex < MAX_CHARS) "" else "..."
            val item = title.substring(range = 0..endIndex)
            Toast.makeText(context, getString(R.string.item_clicked, item, ellipsis),
                Toast.LENGTH_SHORT).show()
        }

        val lm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        scrollListener = object : EndlessRecyclerViewScrollListener(lm) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadProfileData(page)
            }
        }
        rv_requests.apply {
            layoutManager = lm
            adapter = postAdapter
            addOnScrollListener(scrollListener)
        }
    }

    fun populate(posts: List<Post>) {
        postsList.addAll(posts)
        postAdapter.notifyDataSetChanged()

        (activity as MainActivity).updateCountBadge(postsList.size)
    }

    /**
     * Loads the profile data from internet
     * @param pageNo The page number in which the data is to be loaded
     */
    private fun loadProfileData(pageNo: Int) {
        if (pageNo > MAX_PAGES) return

        pb_fragRequest.visibility = View.VISIBLE

        val retrofitInterface = RetrofitInterface.instance
        val call = retrofitInterface.getPosts(pageNo)
        call.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    val posts: List<Post>? = response.body()
                    if (!posts.isNullOrEmpty()) {
                        isDataLoaded = true
                        cPageNo = pageNo
                        populate(posts)
                    } else {
                        tv_fragRequest_error?.text = getString(R.string.no_requests)
                        tv_fragRequest_error.visibility = View.VISIBLE
                    }

                } else {
                    tv_fragRequest_error?.text = getString(R.string.err_unable_to_fetch)
                    tv_fragRequest_error.visibility = View.VISIBLE
                }

                pb_fragRequest?.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                if (!isDataLoaded) {
                    tv_fragRequest_error?.text = getString(R.string.err_unable_to_fetch)
                    tv_fragRequest_error.visibility = View.VISIBLE
                }
                pb_fragRequest?.visibility = View.GONE
                isDataLoaded = false
            }
        })
    }

    /**
     * Register a callback to be invoked when network connectivity changes
     * @return True If internet is available; False otherwise
     */
    private fun registerNetworkChangeCallback(): Boolean {
        val isAvailable = BooleanArray(1)
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        val connectivityManager = context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                // Internet is Available
                activity?.runOnUiThread {
                    isAvailable[0] = true
                    // Load data
                    if (!isDataLoaded)
                        loadProfileData(cPageNo)
                }
            }
        })
        return isAvailable[0]
    }
}