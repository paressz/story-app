package com.farez.storyapp.ui.activity.Story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farez.storyapp.data.remote.response.Story
import com.farez.storyapp.databinding.RvStoryBinding
import com.farez.storyapp.ui.activity.detail.DetailActivity

class StoryPagedAdapter : PagingDataAdapter <Story, StoryPagedAdapter.PagedViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: PagedViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.apply {
                binding.apply {
                    tvItemName.text = data.name
                    Glide.with(holder.itemView.context)
                        .load(data.photoUrl)
                        .into(ivItemPhoto)
                }
                itemView.setOnClickListener {
                    val intent = Intent(it.context, DetailActivity::class.java).putExtra("story", data)
                    val activityTransitioni : ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            it.context as Activity, Pair(binding.ivItemPhoto, "foto"), Pair(binding.tvItemName, "text")
                        )
                    it.context.startActivity(intent,activityTransitioni.toBundle())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedViewHolder {
        val binding = RvStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagedViewHolder(binding)
    }

     class PagedViewHolder (var binding : RvStoryBinding): RecyclerView.ViewHolder(binding.root){
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}