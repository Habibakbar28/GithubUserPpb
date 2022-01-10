package com.dicoding.githubuser.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_favorite")
data class FavoriteEntities(
    @PrimaryKey
    val id: Int?,
    @ColumnInfo(name = "username")
    val username: String?,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "avatar")
    val avatar: String?,
    @ColumnInfo(name = "company")
    val company: String?,
    @ColumnInfo(name = "location")
    val location: String?,
    @ColumnInfo(name = "followers")
    val followers: Int?,
    @ColumnInfo(name = "following")
    val following: Int?,
    @ColumnInfo(name = "repository")
    val repository: Int?,
)