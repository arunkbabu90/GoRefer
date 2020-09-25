package arunkbabu90.gorefer.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import arunkbabu90.gorefer.R
import arunkbabu90.gorefer.fragments.BlankFragment
import arunkbabu90.gorefer.fragments.ProfileFragment
import arunkbabu90.gorefer.model.Post
import arunkbabu90.gorefer.network.RetrofitInterface
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_request.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var profileFrag: ProfileFragment? = null
    var postList: ArrayList<Post> = ArrayList()

    companion object {
        var isDataLoaded = false
        var isFetchError = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        profileFrag = ProfileFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, profileFrag!!)
            .commit()

        loadProfileData()

        bnv_main.setOnNavigationItemSelectedListener(this)
        bnv_main.selectedItemId = R.id.mnu_profile
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnu_alerts, R.id.mnu_chats, R.id.mnu_home -> {
                // Since my scope of this assignment is only Profile Screen; Show the blank fragment
                // on item selection
                profileFrag = null
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, BlankFragment())
                    .commit()
            }
            R.id.mnu_profile -> {
                // When profile menu item is selected Show Profile
                if (!ProfileFragment.isProfileFragmentActive) {
                    profileFrag = ProfileFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, profileFrag!!)
                        .commit()
                }
            }
        }
        return true
    }

    private fun loadProfileData() {
        val retrofitInterface = RetrofitInterface.instance
        val call = retrofitInterface.getPosts()
        call.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    val posts: List<Post>? = response.body()
                    if (!posts.isNullOrEmpty()) {
                        postList.addAll(posts)
                        profileFrag?.populateToRequestFragment()
                        isDataLoaded = true
                        isFetchError = false
                    } else {
                        tv_fragRequest_error?.text = getString(R.string.no_requests)
                        isDataLoaded = true
                        isFetchError = false
                    }

                } else {
                    tv_fragRequest_error?.text = getString(R.string.err_unable_to_fetch)
                    isDataLoaded = false
                    isFetchError = true
                }

                pb_fragRequest?.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                tv_fragRequest_error?.text = getString(R.string.err_unable_to_fetch)
                pb_fragRequest?.visibility = View.GONE
                isDataLoaded = false
                isFetchError = true
            }
        })
    }

    /**
     * Updates the badge with the new item count
     */
    fun updateCountBadge(requestCount: Int) {
        profileFrag?.updateTitleCount(rqCount = requestCount)
    }
}