package com.dicoding.githubuser.data.network

import com.dicoding.githubuser.model.ItemsItem
import com.dicoding.githubuser.model.ResponseDetailUser
import com.dicoding.githubuser.model.UsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiGithub {
    @GET("search/users")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UsersResponse>

    @GET("users/{username}")
    fun getDetailUsers(
        @Path("username") username: String
    ): Call<ResponseDetailUser>

    @GET("users/{username}/followers")
    fun getFollowerUser(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowingUser(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

}