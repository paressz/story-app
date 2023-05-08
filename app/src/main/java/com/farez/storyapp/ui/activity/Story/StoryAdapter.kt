package com.farez.storyapp.ui.activity.Story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farez.storyapp.data.remote.response.Story
import com.farez.storyapp.databinding.RvStoryBinding
import com.farez.storyapp.ui.activity.detail.DetailActivity

class StoryAdapter (private val listStory : List<Story>) : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {
    inner class ListViewHolder (var binding: RvStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = RvStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listStory.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val stories = listStory[position]
        holder.apply {
            binding.apply {
                Glide.with(holder.itemView.context)
                    .load(stories.photoUrl)
                    .into(ivItemPhoto)
                tvItemName.text = stories.name
            }
            itemView.setOnClickListener {
                val intent = Intent(it.context, DetailActivity::class.java).putExtra("story", stories)
                val optionCompat : ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        it.context as Activity, Pair(binding.ivItemPhoto, "foto"), Pair(binding.tvItemName, "name")
                    )
                it.context.startActivity(intent,optionCompat.toBundle())
            }
        }

    }
    private lateinit var onItemClick : OnItemClick
    interface OnItemClick {
        fun itemClicked(data : Story)
    }
    fun setOnItemClick(onItemClick : OnItemClick) {
        this.onItemClick = onItemClick
    }
}