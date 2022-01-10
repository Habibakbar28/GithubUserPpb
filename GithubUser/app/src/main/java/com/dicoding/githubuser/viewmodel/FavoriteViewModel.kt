package com.dicoding.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.local.FavoriteEntities
import com.dicoding.githubuser.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getFavorite(): LiveData<List<FavoriteEntities>> = favoriteRepository.getFavorite()
}