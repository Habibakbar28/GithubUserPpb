package com.dicoding.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.local.FavoriteEntities
import com.dicoding.githubuser.databinding.ItemRowUserBinding
import com.dicoding.githubuser.helper.FavoriteDiffCallback

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private lateinit var onItemClickCallBack: OnItemClickCallBack
    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    private val listFavorite = ArrayList<FavoriteEntities>()
    fun setListFavorite(listFavorite: List<FavoriteEntities>) {
        val diffUtil = FavoriteDiffCallback(this.listFavorite, listFavorite)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.listFavorite.clear()
        this.listFavorite.addAll(listFavorite)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class FavoriteViewHolder(private val binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteEntities: FavoriteEntities) {
            binding.apply {
                Glide.with(itemView)
                    .load(favoriteEntities.avatar)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading))
                    .error(R.drawable.ic_error)
                    .into(imgItemPhoto)
                tvId.text = favoriteEntities.id.toString()
                tvUsername.text = favoriteEntities.username
            }
            itemView.setOnClickListener { onItemClickCallBack.onItemClicked(listFavorite[adapterPosition]) }
            itemView.animation =
                AnimationUtils.loadAnimation(itemView.context, R.anim.recycler_anim)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])

    }

    override fun getItemCount(): Int = listFavorite.size

    interface OnItemClickCallBack {
        fun onItemClicked(favoriteEntities: FavoriteEntities)
    }
}