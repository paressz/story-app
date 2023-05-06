package com.farez.storyapp.ui.activity.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.farez.storyapp.databinding.ActivityMainBinding
import com.farez.storyapp.ui.activity.detail.DetailActivity
import com.farez.storyapp.ui.activity.login.LoginActivity
import com.farez.storyapp.ui.activity.register.RegisterActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            toLoginButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
            toRegisterButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, RegisterActivity::class.java))

            }
            todetail.setOnClickListener {
                startActivity(Intent(this@MainActivity, DetailActivity::class.java))
            }
        }

    }
}