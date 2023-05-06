package com.farez.storyapp.ui.activity.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.farez.storyapp.R
import com.farez.storyapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val email = edLoginEmail.text
            val password = edLoginPassword.text

            toLoginButton.setOnClickListener {
                if (email.isNullOrEmpty() || password.isNullOrEmpty() || password.length < 8) {
                    Toast.makeText(this@LoginActivity, R.string.errorField, Toast.LENGTH_SHORT).show()
                } else {
                    //login
                }

            }
        }
    }
}