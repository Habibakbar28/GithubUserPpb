package com.dicoding.githubuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.local.FavoriteEntities
import com.dicoding.githubuser.data.network.ApiConfig
import com.dicoding.githubuser.model.ResponseDetailUser
import com.dicoding.githubuser.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : ViewModel() {

    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _listDetailUser = MutableLiveData<ResponseDetailUser>()
    val listDetailUser: LiveData<ResponseDetailUser> = _listDetailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var isFavorite = false


    fun getDetailUsers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUsers(username)
        client.enqueue(object : Callback<ResponseDetailUser> {
            override fun onResponse(
                call: Call<ResponseDetailUser>,
                response: Response<ResponseDetailUser>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listDetailUser.value = response.body()
                }
            }

            override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                _isLoading.value = false
                Log.e("DetailUserViewModel", "on Failure : ${t.message.toString()}")
            }
        })
    }

    fun addFavorite(favoriteEntities: FavoriteEntities) {
        favoriteRepository.addFavorite(favoriteEntities)
    }

    fun deleteFavorite(id: Int) {
        favoriteRepository.deleteFavorite(id)
    }

    fun getIdFavorite(id: Int): LiveData<List<FavoriteEntities>> = favoriteRepository.getIdFavorite(id)
}