package com.dicoding.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.data.local.FavoriteDao
import com.dicoding.githubuser.data.local.FavoriteDatabase
import com.dicoding.githubuser.data.local.FavoriteEntities
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val favoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getDatabase(application)
        favoriteDao = db.favoriteDao()
    }

    fun addFavorite(favoriteEntities: FavoriteEntities) {
        executorService.execute {
            favoriteDao.addFavorite(favoriteEntities)
        }
    }

    fun deleteFavorite(id: Int) {
        executorService.execute {
            favoriteDao.deleteFavorite(id)
        }
    }

    fun getIdFavorite(id: Int): LiveData<List<FavoriteEntities>> = favoriteDao.getIdFavorite(id)

    fun getFavorite(): LiveData<List<FavoriteEntities>> = favoriteDao.getFavorite()
}