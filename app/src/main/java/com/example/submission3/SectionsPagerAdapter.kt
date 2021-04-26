package com.example.submission3

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.submission3.custom.CustomViewPager
import com.example.submission3.follower.FollowerFragment
import com.example.submission3.following.FollowingFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    lateinit var cur_user_pager : User
    private var mCurrentPosition = -1
    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.follower,
        R.string.following)

    fun setMainUser(param_user : User){
        cur_user_pager = param_user
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        if (position != mCurrentPosition) {
            val fragment = `object` as Fragment
            val pager: CustomViewPager = container as CustomViewPager
            if (fragment.view != null) {
                mCurrentPosition = position
                pager.measureCurrentView(fragment.view)
            }
        }
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowerFragment.newInstance(cur_user_pager)
            1 -> fragment = FollowingFragment.newInstance(cur_user_pager)
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}