package com.farez.storyapp.ui.activity.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.farez.storyapp.data.remote.response.Story
import com.farez.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val story = intent.getParcelableExtra<Story>("story")
        Glide.with(this)
            .load(story?.photoUrl)
            .override(Target.SIZE_ORIGINAL)
            .fitCenter()
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = story?.name
        binding.tvDetailDescription.text = story?.description
    }
}