package com.dicoding.githubuser.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.helper.SettingPreferences
import com.dicoding.githubuser.model.ItemsItem
import com.dicoding.githubuser.ui.adapter.UserAdapter
import com.dicoding.githubuser.ui.detail.DetailUserActivity
import com.dicoding.githubuser.ui.favorite.FavoriteActivity
import com.dicoding.githubuser.ui.setting.SettingActivity
import com.dicoding.githubuser.viewmodel.SearchViewModel
import com.dicoding.githubuser.viewmodel.SettingModelFactory
import com.dicoding.githubuser.viewmodel.SettingViewModel

class MainActivity : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this, { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[SearchViewModel::class.java]


        showRecyclerUser()

        binding.apply {
            btnSearch.setOnClickListener {
                searchUser()
            }
            edtQuery.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        viewModel.isLoading.observe(this, {
            showLoading(it)
        })
    }

    private fun showRecyclerUser() {
        binding.apply {
            rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = UserAdapter()
            rvUsers.setHasFixedSize(true)
            rvUsers.adapter = adapter

            viewModel.listUsers.observe(this@MainActivity, {
                if (it != null) {
                    adapter.setList(it)
                }
                if (it.isEmpty()) {
                    searchAnim.visibility = View.VISIBLE
                    tvSearchUser.visibility = View.VISIBLE
                    tvSearchUser.text = getString(R.string.user_not_found)
                } else {
                    searchAnim.visibility = View.GONE
                    tvSearchUser.visibility = View.GONE
                }
            })

            adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ItemsItem) {
                    val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                    intent.putExtra(EXTRA_USER, data.login)
                    startActivity(intent)
                }
            })
        }
    }

    private fun searchUser() {
        binding.apply {
            val query = edtQuery.text.toString()
            if (query.isEmpty()) return
            showLoading(true)
            viewModel.setSearchUsers(query)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.apply {
            if (state) {
                progresBar.visibility = View.VISIBLE
                searchAnim.visibility = View.GONE
                tvSearchUser.visibility = View.GONE
            } else {
                progresBar.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.user_favorite -> {
                Intent(this, FavoriteActivity::class.java).apply {
                    startActivity(this)
                }
                true
            }
            R.id.main_setting -> {
                Intent(this, SettingActivity::class.java).apply {
                    startActivity(this)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}