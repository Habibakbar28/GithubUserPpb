package com.dicoding.githubuser.ui.followers

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
import com.dicoding.githubuser.databinding.FragmentFollowBinding
import com.dicoding.githubuser.model.ItemsItem
import com.dicoding.githubuser.ui.adapter.UserAdapter
import com.dicoding.githubuser.ui.detail.DetailUserActivity
import com.dicoding.githubuser.ui.main.MainActivity
import com.dicoding.githubuser.viewmodel.FollowerViewModel

class FollowersFragment : Fragment() {
    private lateinit var username: String
    private lateinit var followerAdapter: UserAdapter
    private lateinit var rvFollower: RecyclerView
    private lateinit var followerViewModel: FollowerViewModel
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding as FragmentFollowBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowBinding.bind(view)
        username = arguments?.getString(DetailUserActivity.EXTRA_USERNAME).toString()

        followerViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[FollowerViewModel::class.java]

        followerViewModel.getFollowerUser(username)
        followerViewModel.listFollower.observe(viewLifecycleOwner, {
            if (it != null) {
                showDataFollower(it)
            }
            binding.apply {
                if (it.isEmpty()) {
                    emptyFollowers.visibility = View.VISIBLE
                    tvNoFollowers.visibility = View.VISIBLE
                } else {
                    emptyFollowers.visibility = View.GONE
                    tvNoFollowers.visibility = View.GONE
                }
            }
        })

        followerViewModel.isLoading.observe(viewLifecycleOwner, {
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

    private fun showDataFollower(it: List<ItemsItem>) {
        rvFollower = binding.rvFollow
        rvFollower.layoutManager = LinearLayoutManager(activity)
        followerAdapter = UserAdapter()
        followerAdapter.setList(it)
        rvFollower.setHasFixedSize(true)
        rvFollower.adapter = followerAdapter

        followerAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val intent = Intent(activity, DetailUserActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_USER, data.login)
                startActivity(intent)
            }
        })
    }
}