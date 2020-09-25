package arunkbabu90.gorefer.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import arunkbabu90.gorefer.R
import arunkbabu90.gorefer.fragments.BlankFragment
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
                profileFrag = ProfileFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, profileFrag!!)
                    .commit()
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