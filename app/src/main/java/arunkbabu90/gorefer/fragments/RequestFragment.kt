package arunkbabu90.gorefer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import arunkbabu90.gorefer.R
import arunkbabu90.gorefer.activities.MainActivity
import arunkbabu90.gorefer.adapter.PostAdapter
import arunkbabu90.gorefer.model.Post
import kotlinx.android.synthetic.main.fragment_request.*

class RequestsFragment : Fragment() {
    private lateinit var adapter: PostAdapter
    private var postsList: ArrayList<Post> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = PostAdapter(postsList) { post: Post ->
            // Item Click listener
            val MAX_CHARS = 40
            val title = post.title
            val endIndex = if (title.length < MAX_CHARS) title.length - 1 else MAX_CHARS
            val ellipsis = if (endIndex < MAX_CHARS) "" else "..."
            val item = title.substring(range = 0..endIndex)
            Toast.makeText(context, getString(R.string.item_clicked, item, ellipsis), Toast.LENGTH_SHORT).show()
        }
        rv_requests.layoutManager = lm
        rv_requests.adapter = adapter

        if (MainActivity.isDataLoaded) {
            populate()

            if (postsList.isNullOrEmpty() && !MainActivity.isFetchError)
                tv_fragRequest_error?.text = getString(R.string.no_requests)
        } else {
            pb_fragRequest.visibility = View.VISIBLE
        }

        if (MainActivity.isFetchError)
            tv_fragRequest_error.text = getString(R.string.err_unable_to_fetch)
    }

    fun populate() {
        val ma: MainActivity = (activity as MainActivity)
        val posts = ma.postList
        postsList.addAll(posts)
        adapter.notifyDataSetChanged()

        ma.updateCountBadge(posts.size)
    }
}