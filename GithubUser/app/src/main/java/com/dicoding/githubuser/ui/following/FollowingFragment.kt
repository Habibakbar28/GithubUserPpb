package com.dicoding.githubuser.ui.following

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.FragmentFollowingBinding
import com.dicoding.githubuser.model.ItemsItem
import com.dicoding.githubuser.ui.adapter.UserAdapter
import com.dicoding.githubuser.ui.detail.DetailUserActivity
import com.dicoding.githubuser.ui.main.MainActivity
import com.dicoding.githubuser.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {
    private lateinit var username: String
    private lateinit var followingAdapter: UserAdapter
    private lateinit var rvFollowing: RecyclerView
    private lateinit var followingViewModel: FollowingViewModel
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding as FragmentFollowingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowingBinding.bind(view)
        username = arguments?.getString(DetailUserActivity.EXTRA_USERNAME).toString()

        followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowingViewModel::class.java]

        followingViewModel.getFollowingUser(username)
        followingViewModel.listFollowing.observe(viewLifecycleOwner, {
            if (it != null) {
                showDataFollowing(it)
            }
            binding.apply {
                if (it.isEmpty()) {
                    emptyFollowing.visibility = View.VISIBLE
                    tvNoFollowing.visibility = View.VISIBLE
                } else {
                    emptyFollowing.visibility = View.GONE
                    tvNoFollowing.visibility = View.GONE
                }
            }
        })

        followingViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progresBar.visibility = View.VISIBLE
        } else {
            binding.progresBar.visibility = View.GONE
        }
    }

    private fun showDataFollowing(it: List<ItemsItem>) {
        rvFollowing = binding.rvFollowing
        rvFollowing.layoutManager = LinearLayoutManager(activity)
        followingAdapter = UserAdapter()
        followingAdapter.setList(it)
        rvFollowing.setHasFixedSize(true)
        rvFollowing.adapter = followingAdapter

        followingAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val intent = Intent(activity, DetailUserActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_USER, data.login)
                startActivity(intent)
            }
        })
    }
}