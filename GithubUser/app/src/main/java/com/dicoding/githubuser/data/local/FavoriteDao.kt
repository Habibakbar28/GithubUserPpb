package com.dicoding.githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert
    fun addFavorite(favoriteEntities: FavoriteEntities)

    @Query("DELETE FROM tb_favorite WHERE id = :id")
    fun deleteFavorite(id: Int)

    @Query("SELECT * FROM tb_favorite")
    fun getFavorite(): LiveData<List<FavoriteEntities>>

    @Query("SELECT * FROM tb_favorite WHERE id = :id")
    fun getIdFavorite(id: Int): LiveData<List<FavoriteEntities>>
}