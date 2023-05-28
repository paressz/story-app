package com.farez.storyapp.ui.activity.Story

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farez.storyapp.data.remote.response.Story
import com.farez.storyapp.databinding.RvStoryBinding

class StoryPagedAdapter : PagingDataAdapter <Story, StoryPagedAdapter.PagedViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: PagedViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null ) {
            holder.bind(data)
        }
        holder.binding.apply {
            tvItemName.text = data?.name
            Glide.with(holder.itemView.context)
                .load(data?.photoUrl)
                .into(ivItemPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedViewHolder {
        val binding = RvStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagedViewHolder(binding)
    }

     class PagedViewHolder (var binding : RvStoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind (data : Story){

        }
    }
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}