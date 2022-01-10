package com.dicoding.githubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.local.FavoriteEntities
import com.dicoding.githubuser.databinding.ActivityFavoriteBinding
import com.dicoding.githubuser.ui.adapter.FavoriteAdapter
import com.dicoding.githubuser.ui.detail.DetailUserActivity
import com.dicoding.githubuser.ui.detail.DetailUserActivity.Companion.EXTRA_USERNAME
import com.dicoding.githubuser.ui.main.MainActivity.Companion.EXTRA_USER
import com.dicoding.githubuser.ui.setting.SettingActivity
import com.dicoding.githubuser.viewmodel.FavoriteViewModel
import com.dicoding.githubuser.viewmodel.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.user_favorite)

        favoriteViewModel = obtainViewModel(this)

        favoriteViewModel.getFavorite().observe(this, {
            showRecyclerFavorite(it)
            dataEmpty(it)
        })
    }

    private fun showRecyclerFavorite(listFavorite: List<FavoriteEntities>) {
        binding.apply {
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            val favoriteAdapter = FavoriteAdapter()
            favoriteAdapter.setListFavorite(listFavorite)
            rvFavorite.setHasFixedSize(true)
            rvFavorite.adapter = favoriteAdapter

            favoriteAdapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallBack {
                override fun onItemClicked(favoriteEntities: FavoriteEntities) {
                    Intent(this@FavoriteActivity, DetailUserActivity::class.java).apply {
                        putExtra(EXTRA_USER, favoriteEntities.username)
                        putExtra(EXTRA_USERNAME, favoriteEntities.username)
                        startActivity(this)
                    }
                }
            })
        }
    }

    private fun dataEmpty(data: List<FavoriteEntities>) {
        if (data.isEmpty()) {
            binding.emptyFavorite.visibility = View.VISIBLE
            binding.tvNoFavorite.visibility = View.VISIBLE
        } else {
            binding.emptyFavorite.visibility = View.GONE
            binding.tvNoFavorite.visibility = View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite_setting -> {
                Intent(this, SettingActivity::class.java).apply {
                    startActivity(this)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}