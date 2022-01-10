package com.dicoding.githubuser.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.githubuser.model.ItemsItem

class UserDiffCallback(
    private val mOldUserList: List<ItemsItem>,
    private val mNewUserList: List<ItemsItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = mOldUserList.size

    override fun getNewListSize(): Int = mNewUserList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUserList[oldItemPosition].login == mNewUserList[newItemPosition].login
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUsers = mOldUserList[oldItemPosition]
        val newUsers = mNewUserList[newItemPosition]
        return oldUsers.login == newUsers.login && oldUsers.id == newUsers.id && oldUsers.avatarUrl == newUsers.avatarUrl
    }
}