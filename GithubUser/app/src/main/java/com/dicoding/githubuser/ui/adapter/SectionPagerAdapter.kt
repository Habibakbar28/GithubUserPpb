package com.dicoding.githubuser.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubuser.ui.followers.FollowersFragment
import com.dicoding.githubuser.ui.following.FollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity, fragmentBundle: Bundle) :
    FragmentStateAdapter(activity) {
    private var fBundle: Bundle = fragmentBundle

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        fragment?.arguments = this.fBundle
        return fragment as Fragment
    }
}