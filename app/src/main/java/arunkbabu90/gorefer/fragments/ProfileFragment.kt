package arunkbabu90.gorefer.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import arunkbabu90.gorefer.R
import arunkbabu90.gorefer.adapter.CategoryAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var mTarget: CustomTarget<Drawable>
    private var tabLayoutMediator: TabLayoutMediator? = null

    private var requestsCount = 0
    private var offeringsCount = 0
    private var recommendsCount = 0

    private val animFabClose: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.fab_close_animation) }
    private val animFabOpen: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.fab_open_animation) }
    private val animPullUp: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.pull_up_animation) }
    private val animPullDown: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.pull_down_animation) }

    private var isFabClicked = false

    companion object {
        var isProfileFragmentActive = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (context != null) {
            categoryAdapter = CategoryAdapter(this)
            vp_profile.adapter = categoryAdapter
        }

        tabLayoutMediator = TabLayoutMediator(tab_profile, vp_profile) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.requests, requestsCount)
                1 -> getString(R.string.offerings, offeringsCount)
                2 -> getString(R.string.recommends, recommendsCount)
                else -> ""
            }
        }
        tabLayoutMediator?.attach()

        // Load profile picture
        loadProfilePicture(R.drawable.sample_dp)

        fab_add.setOnClickListener(this)
        fab_looking.setOnClickListener(this)
        fab_offerings.setOnClickListener(this)
        fab_recommendations.setOnClickListener(this)
        btn_invite.setOnClickListener(this)
        btn_other_settings.setOnClickListener(this)

        isProfileFragmentActive = true
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_invite -> Toast.makeText(context, getString(R.string.invite_frnds), Toast.LENGTH_SHORT).show()

            R.id.btn_other_settings -> Toast.makeText(context, getString(R.string.other_settings), Toast.LENGTH_SHORT).show()

            R.id.fab_add -> {
                if (!isFabClicked)
                    showFabs()
                else
                    hideFabs()

                isFabClicked = !isFabClicked
            }

            R.id.fab_looking -> Toast.makeText(context, getString(R.string.looking_click), Toast.LENGTH_SHORT).show()

            R.id.fab_offerings -> Toast.makeText(context, getString(R.string.offering_click), Toast.LENGTH_SHORT).show()

            R.id.fab_recommendations -> Toast.makeText(context, getString(R.string.recommendation_click), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Loads the image to view
     */
    private fun loadProfilePicture(@DrawableRes drawable: Int) {
        mTarget = object : CustomTarget<Drawable>() {
            override fun onLoadStarted(placeholder: Drawable?) {
                super.onLoadStarted(placeholder)
                iv_profile_dp?.showProgressBar()
            }

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                iv_profile_dp?.setImageDrawable(resource)
                iv_profile_dp?.hideProgressBar()
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                iv_profile_dp?.setImageDrawable(null)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                iv_profile_dp?.setImageDrawable(errorDrawable)
                iv_profile_dp?.hideProgressBar()
            }

        }

        Glide.with(this).load(drawable)
            .placeholder(R.drawable.default_dp)
            .error(R.drawable.default_dp)
            .into(mTarget)
    }

    /**
     * Show all the three small Floating Action buttons with an animation as if they are emanating
     * from the (+) Fab
     */
    private fun showFabs() {
        fab_offerings?.visibility = View.VISIBLE
        fab_recommendations?.visibility = View.VISIBLE
        fab_looking?.visibility = View.VISIBLE

        fab_offerings?.startAnimation(animPullUp)
        fab_recommendations?.startAnimation(animPullUp)
        fab_looking?.startAnimation(animPullUp)
        fab_add?.startAnimation(animFabOpen)

        fab_looking?.isClickable = true
        fab_offerings?.isClickable = true
        fab_recommendations?.isClickable = true
    }

    /**
     * Hides all the three small Floating Action buttons with an animation as if they are collapsing
     * to the (+) Fab
     */
    private fun hideFabs() {
        fab_offerings?.visibility = View.GONE
        fab_recommendations?.visibility = View.GONE
        fab_looking?.visibility = View.GONE

        fab_offerings?.startAnimation(animPullDown)
        fab_recommendations?.startAnimation(animPullDown)
        fab_looking?.startAnimation(animPullDown)
        fab_add?.startAnimation(animFabClose)

        fab_looking?.isClickable = false
        fab_offerings?.isClickable = false
        fab_recommendations?.isClickable = false
    }

    /**
     * Update the right side counter in the title (Ex: Offerings(0))
     * @param rqCount The count to be set in "Request" Title
     * @param ofrCount The count to be set in "Offerings" Title
     * @param recCount The count to be set in "Recommends" Title
     */
    fun updateTitleCount(rqCount: Int = requestsCount, ofrCount: Int = offeringsCount, recCount: Int = recommendsCount) {
        requestsCount = rqCount
        offeringsCount = ofrCount
        recommendsCount = recCount

        val requestsTab = tab_profile.getTabAt(0)
        requestsTab?.text = getString(R.string.requests, requestsCount)

        val offeringsTab = tab_profile.getTabAt(1)
        offeringsTab?.text = getString(R.string.requests, offeringsCount)

        val recommendsTab = tab_profile.getTabAt(2)
        recommendsTab?.text = getString(R.string.requests, recommendsCount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isProfileFragmentActive = false
    }
}