package arunkbabu90.gorefer.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import arunkbabu90.gorefer.fragments.OfferingsFragment
import arunkbabu90.gorefer.fragments.RecommendFragment
import arunkbabu90.gorefer.fragments.RequestsFragment

class CategoryAdapter(frag: Fragment) : FragmentStateAdapter(frag) {
    companion object {
        private const val NUM_PAGES = 3
    }

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RequestsFragment()
            1 -> OfferingsFragment()
            2 -> RecommendFragment()
            else -> RequestsFragment()
        }
    }
}