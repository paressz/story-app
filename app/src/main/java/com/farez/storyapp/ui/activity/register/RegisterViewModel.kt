package com.farez.storyapp.ui.activity.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.farez.storyapp.data.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel()  {
    fun register( name : String, password : String,  email : String) = userRepository.register(name, email, password)
}

class RegisterVMFactory (private var userRepository: UserRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(userRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}