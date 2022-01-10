package com.dicoding.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.network.ApiConfig
import com.dicoding.githubuser.model.ItemsItem
import com.dicoding.githubuser.model.UsersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    private val _listUsers = MutableLiveData<List<ItemsItem>>()
    val listUsers: LiveData<List<ItemsItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setSearchUsers(query: String) {
        _isLoading.value = true
        ApiConfig.getApiService()
            .getSearchUsers(query)
            .enqueue(object : Callback<UsersResponse> {
                override fun onResponse(
                    call: Call<UsersResponse>,
                    response: Response<UsersResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _listUsers.value = response.body()?.items
                    }
                }

                override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "on Failure : ${t.message.toString()}")
                }
            })
    }

    companion object {
        private const val TAG = "SearchViewModel"
    }
}