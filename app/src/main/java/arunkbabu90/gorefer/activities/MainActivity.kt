package arunkbabu90.gorefer.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import arunkbabu90.gorefer.R
import arunkbabu90.gorefer.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var profileFrag: ProfileFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profileFrag = ProfileFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, profileFrag!!)
            .commit()

        bnv_main.setOnNavigationItemSelectedListener(this)
        bnv_main.selectedItemId = R.id.mnu_profile
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnu_alerts, R.id.mnu_chats, R.id.mnu_home, R.id.mnu_profile -> {
                // Since my scope of this assignment is only Profile Screen; only show the profile
                // screen, on what ever navigation item is clicked
                if (!ProfileFragment.isProfileFragmentActive) {
                    // Don't create the fragment more than once if it's already loaded
                    profileFrag = ProfileFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, profileFrag!!)
                        .commit()
                }
            }
        }
        return true
    }

    /**
     * Updates the badge with the new item count
     */
    fun updateCountBadge(requestCount: Int) {
        profileFrag?.updateTitleCount(rqCount = requestCount)
    }
}