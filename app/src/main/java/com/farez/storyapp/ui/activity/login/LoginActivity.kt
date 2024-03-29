package com.farez.storyapp.ui.activity.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.farez.storyapp.R
import com.farez.storyapp.api.ApiConfig
import com.farez.storyapp.data.local.preferences.LoginPreferences
import com.farez.storyapp.data.remote.Result
import com.farez.storyapp.data.repository.UserRepository
import com.farez.storyapp.databinding.ActivityLoginBinding
import com.farez.storyapp.ui.activity.Story.StoryActivity
import com.farez.storyapp.ui.activity.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "TOKEN")
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var vmFactory: LoginVMFactory
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vmFactory = LoginVMFactory(UserRepository.getInstance(ApiConfig.getApiService()), LoginPreferences.getInstance(dataStore))
        loginViewModel = ViewModelProvider(this, vmFactory)[LoginViewModel::class.java]
        supportActionBar?.hide()
        setup()
    }

    override fun onResume() {
        super.onResume()
    }
     fun setup() {
         binding.apply {
             loginButton.setOnClickListener {
                 val email = edLoginEmail.text.toString()
                 val password = edLoginPassword.text.toString()
                 if (!validEmail(email) || !validPass(password)) {
                     Toast.makeText(this@LoginActivity, R.string.errorField, Toast.LENGTH_SHORT).show()
                 } else {
                     loginViewModel.login(email, password).observe(this@LoginActivity) {
                        when (it) {
                            is Result.Success -> {
                                progressBar.visibility = View.GONE
                                loginViewModel.setAuth(true)
                                loginViewModel.saveToken(it.data.loginResult.token)
                                startActivity(Intent(this@LoginActivity, StoryActivity::class.java))
                                finish()
                            }
                            is Result.Error -> {
                                progressBar.visibility = View.GONE
                                Toast.makeText(this@LoginActivity, it.error, Toast.LENGTH_SHORT).show()
                            }
                            is Result.Loading -> {
                                progressBar.visibility = View.VISIBLE

                            }
                        }
                     }
                 }

             }
         }
     }

    fun checkAuth() {
        loginViewModel.getAuth().observe(this) {
            if (!it) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }
    fun validEmail(email : String) : Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun validPass(pass : String) : Boolean {
        return (pass.length >= 8)
    }
}