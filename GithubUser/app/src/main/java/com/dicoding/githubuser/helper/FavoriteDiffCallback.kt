package com.dicoding.githubuser.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.githubuser.data.local.FavoriteEntities

class FavoriteDiffCallback(
    private val oldFavoriteList: List<FavoriteEntities>,
    private val newFavoriteList: List<FavoriteEntities>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFavoriteList.size

    override fun getNewListSize(): Int = newFavoriteList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteList[oldItemPosition].id == newFavoriteList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = oldFavoriteList[oldItemPosition]
        val newFavorite = newFavoriteList[newItemPosition]

        return oldFavorite.id == newFavorite.id && oldFavorite.username == newFavorite.username && oldFavorite.name == newFavorite.name && oldFavorite.avatar == newFavorite.avatar
    }
}