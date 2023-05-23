package com.farez.storyapp.ui.activity.Story

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.farez.storyapp.R
import com.farez.storyapp.api.ApiConfig
import com.farez.storyapp.data.local.preferences.LoginPreferences
import com.farez.storyapp.data.repository.StoryRepository
import com.farez.storyapp.databinding.ActivityStoryBinding
import com.farez.storyapp.data.remote.Result
import com.farez.storyapp.ui.activity.Upload.UploadActivity
import com.farez.storyapp.ui.activity.main.MainActivity
import com.farez.storyapp.ui.activity.map.MapsActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "TOKEN")
class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var vmFactory: StoryVMFactory
    private lateinit var token : String
    private var isExpanded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        fabSetup()
        getToken()

    }

    override fun onResume() {
        super.onResume()
        getToken()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finish()
    }
    fun setupViewModel() {
        vmFactory = StoryVMFactory(StoryRepository.getInstance(ApiConfig.getApiService()),
            LoginPreferences.getInstance(dataStore))
        storyViewModel = ViewModelProvider(this, vmFactory)[StoryViewModel::class.java]
    }
    fun getToken() {
        storyViewModel.getToken().observe(this) {
            if (it == "null") {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                token = "Bearer $it"
                setupView(token)
            }
        }
    }
    fun setupView(token : String) {
        supportActionBar?.hide()
        binding.apply {
            storyViewModel.getStories(token).observe(this@StoryActivity) {
                when (it) {
                    is Result.Loading -> {
                        progressBar2.visibility =  View.VISIBLE
                    }
                    is Result.Error -> {
                        progressBar2.visibility =  View.GONE
                        val error =  it.error
                        Toast.makeText(this@StoryActivity, error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        progressBar2.visibility =  View.GONE
                        textView3.visibility = View.GONE
                        if (it.data.isEmpty()) {
                            textView3.visibility = View.VISIBLE
                            Toast.makeText(this@StoryActivity, "${it.data.isEmpty()}", Toast.LENGTH_SHORT).show()
                        } else {
                            rv.adapter = StoryAdapter(it.data)
                            if (this@StoryActivity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                rv.layoutManager = GridLayoutManager(this@StoryActivity,2)

                            } else {
                                rv.layoutManager = LinearLayoutManager(this@StoryActivity)
                            }
                        }
                    }
                }
            }
        }
    }
    fun fabExpand() {
        if (isExpanded) {
            binding.apply {
                floatingActionButton2.visibility = View.VISIBLE
                floatingActionButton3.visibility = View.VISIBLE
                floatingActionButton4.visibility = View.VISIBLE
                floatingActionButton.setImageResource(R.drawable.round_close_24)
            }
        }
        else {
            binding.apply {
                floatingActionButton2.visibility = View.GONE
                floatingActionButton3.visibility = View.GONE
                floatingActionButton4.visibility = View.GONE

                floatingActionButton.setImageResource(R.drawable.round_add_24)
            }
        }
    }
    fun fabSetup() {
        binding.apply {
            floatingActionButton.setOnClickListener {
                isExpanded = !isExpanded
                fabExpand()
            }
            floatingActionButton2.setOnClickListener {
                val sendToken = Intent(this@StoryActivity, UploadActivity::class.java)
                sendToken.putExtra("token", token)
                startActivity(sendToken)
            }
            floatingActionButton3.setOnClickListener {
                storyViewModel.logout()
            }
            floatingActionButton4.setOnClickListener {
                val sendToken = Intent(this@StoryActivity, MapsActivity::class.java).putExtra("token", token)
                startActivity(sendToken)
            }
        }
    }

}