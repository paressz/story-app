package com.farez.storyapp.ui.activity.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.farez.storyapp.data.local.preferences.LoginPreferences
import com.farez.storyapp.databinding.ActivityMainBinding
import com.farez.storyapp.ui.activity.Story.StoryActivity
import com.farez.storyapp.ui.activity.login.LoginActivity
import com.farez.storyapp.ui.activity.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "TOKEN")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var vmFactory: MainVMFactory
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setupViewModel()
        checkAuth()
        binding.apply {
            toLoginButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
            toRegisterButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, RegisterActivity::class.java))

            }
        }
        playAnime()
    }
    fun setupViewModel() {
        vmFactory = MainVMFactory(LoginPreferences.getInstance(dataStore))
        mainViewModel = ViewModelProvider(this, vmFactory)[MainViewModel::class.java]
    }
    fun playAnime() {
        binding.apply {
            val login = ObjectAnimator.ofFloat(toLoginButton, View.ALPHA, 1f).setDuration(600)
            val regiser = ObjectAnimator.ofFloat(toRegisterButton, View.ALPHA, 1f).setDuration(600)
            val welcome = ObjectAnimator.ofFloat(textView, View.ALPHA, 1f).setDuration(600)
            val text = ObjectAnimator.ofFloat(textView2, View.ALPHA, 1f).setDuration(600)

            val together = AnimatorSet().apply {
                playTogether(welcome, text)
            }
            val together2 = AnimatorSet().apply {
                playTogether(login, regiser)
            }
            AnimatorSet().apply {
                playSequentially(together, together2)
                start()
            }
        }
    }
    fun checkAuth() {
        mainViewModel.getAuth().observe(this) {
            if (it) {
                startActivity(Intent(this@MainActivity, StoryActivity::class.java))
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finish()
    }
}