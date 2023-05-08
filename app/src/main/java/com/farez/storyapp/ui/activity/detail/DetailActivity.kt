package com.farez.storyapp.ui.activity.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.farez.storyapp.data.remote.response.Story
import com.farez.storyapp.databinding.ActivityDetailBinding
import com.bumptech.glide.request.target.Target

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
            .into(binding.ivDetailPhoto)
        binding.tvDetailName.text = story?.name
        binding.tvDetailDescription.text = story?.description
    }
}