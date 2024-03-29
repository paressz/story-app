package com.farez.storyapp.ui.activity.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.farez.storyapp.R
import com.farez.storyapp.api.ApiConfig
import com.farez.storyapp.data.repository.UserRepository
import com.farez.storyapp.databinding.ActivityRegisterBinding
import com.farez.storyapp.ui.activity.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var vmFactory: RegisterVMFactory
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityRegisterBinding.inflate(layoutInflater)
            setContentView(binding.root)
            vmFactory = RegisterVMFactory(UserRepository.getInstance(ApiConfig.getApiService()))
            registerViewModel = ViewModelProvider(this, vmFactory).get(RegisterViewModel::class.java)
            binding.apply {
                registerButton.setOnClickListener {
                    val email : String = edRegisterEmail.text.toString()
                    val password : String= edRegisterPassword.text.toString()
                    val name : String = edRegisterName.text.toString()
                    if (!validEmail(email) || !validPass(password) || name.isEmpty()) {
                        Toast.makeText(this@RegisterActivity, R.string.errorField, Toast.LENGTH_SHORT).show()
                    } else {
                        registerViewModel.register(name, password, email)
                        Toast.makeText(this@RegisterActivity, R.string.registerSuccess, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    }


                }
            }
    }
    fun validEmail(email : CharSequence) : Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun validPass(pass : String) : Boolean {
        return (pass.length >=8)
    }
}