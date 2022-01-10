package com.dicoding.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ItemRowUserBinding
import com.dicoding.githubuser.helper.UserDiffCallback
import com.dicoding.githubuser.model.ItemsItem

class UserAdapter :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null
    private val listUser = ArrayList<ItemsItem>()

    fun setList(users: List<ItemsItem>) {
        val diffCallback = UserDiffCallback(this.listUser, users)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listUser.clear()
        this.listUser.addAll(users)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvUsername: TextView = binding.tvUsername
        val tvId: TextView = binding.tvId
        val imgAvatar: ImageView = binding.imgItemPhoto
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view =
            ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: UserAdapter.ViewHolder, position: Int) {
        val data = listUser[position]
        viewHolder.apply {
            tvUsername.text = data.login
            tvId.text = data.id.toString()
            Glide.with(itemView.context)
                .load(data.avatarUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                .error(R.drawable.ic_error)
                .into(imgAvatar)
            itemView.setOnClickListener { onItemClickCallback?.onItemClicked(listUser[position]) }
            itemView.animation =
                AnimationUtils.loadAnimation(itemView.context, R.anim.recycler_anim)
        }
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

}