package com.dicoding.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.local.FavoriteEntities
import com.dicoding.githubuser.databinding.ActivityDetailUserBinding
import com.dicoding.githubuser.model.ResponseDetailUser
import com.dicoding.githubuser.ui.adapter.SectionPagerAdapter
import com.dicoding.githubuser.ui.main.MainActivity
import com.dicoding.githubuser.ui.setting.SettingActivity
import com.dicoding.githubuser.viewmodel.DetailUserViewModel
import com.dicoding.githubuser.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var username: String
    private lateinit var detailUserViewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.detail_user)

        detailUserViewModel = obtainViewModel(this)

        username = intent.getStringExtra(MainActivity.EXTRA_USER) as String

        detailUserViewModel.getDetailUsers(username)
        detailUserViewModel.isLoading.observe(this, {
            showLoading(it)
        })
        detailUserViewModel.listDetailUser.observe(this, {
            setDataToView(it)
        })

        viewPager()
    }

    private fun viewPager() {
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)
        val sectionsPagerAdapter = SectionPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = binding.vpDetailUser
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tlDetailUser
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun setDataToView(it: ResponseDetailUser?) {
        binding.apply {
            if (it != null) {
                Glide.with(this@DetailUserActivity)
                    .load(it.avatarUrl)
                    .into(binding.circleImageView)
                username.text = getString(R.string.brackets, it.login)
                nama.text = it.name
                if (it.name == null) {
                    nama.text = getString(R.string.no_name)
                }
                lokasi.text = it.location
                if (it.location == null) {
                    lokasi.text = getString(R.string.no_location)
                }
                company.text = it.company
                if (it.company == null) {
                    company.text = getString(R.string.no_company)
                }
                respositori.text = it.publicRepos.toString()
                follower.text = it.followers.toString()
                following.text = it.following.toString()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progresBar.visibility = View.VISIBLE
        } else {
            binding.progresBar.visibility = View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailUserViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_favorite -> {
                detailUserViewModel.apply {
                    isFavorite = if (isFavorite) {
                        (R.drawable.ic_favorite_white_24)
                        listDetailUser.observe(this@DetailUserActivity, {
                            item.setIcon(R.drawable.ic_favorite_white_24)
                            deleteFavorite(it.id)
                            Toast.makeText(
                                this@DetailUserActivity,
                                "${it.name} ${resources.getString(R.string.delete_favorite)}",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                        false
                    } else {
                        item.setIcon(R.drawable.ic_favorite_red_24)
                        listDetailUser.observe(this@DetailUserActivity, {
                            addFavorite(
                                FavoriteEntities(
                                    id = it.id,
                                    username = it.login,
                                    name = it.name,
                                    avatar = it.avatarUrl,
                                    company = it.company,
                                    location = it.location,
                                    followers = it.followers,
                                    following = it.following,
                                    repository = it.publicRepos
                                )
                            )
                            Toast.makeText(
                                this@DetailUserActivity,
                                "${it.name} ${resources.getString(R.string.add_favorite)}",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                        true
                    }
                }
                true
            }
            R.id.detail_setting -> {
                Intent(this, SettingActivity::class.java).apply {
                    startActivity(this)
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val favorite = menu?.findItem(R.id.btn_favorite)
        detailUserViewModel.apply {
            listDetailUser.observe(this@DetailUserActivity, {
                getIdFavorite(it.id)
                    .observe(this@DetailUserActivity, { data ->
                        isFavorite = if (data.isNotEmpty()) {
                            favorite?.setIcon(R.drawable.ic_favorite_red_24)
                            true
                        } else {
                            favorite?.setIcon(R.drawable.ic_favorite_white_24)
                            false
                        }
                    })
            })
        }
        return super.onPrepareOptionsMenu(menu)
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_1, R.string.tab_2)
    }

}