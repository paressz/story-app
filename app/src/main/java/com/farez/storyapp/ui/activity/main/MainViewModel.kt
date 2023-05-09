package com.farez.storyapp.ui.activity.main

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.farez.storyapp.data.local.preferences.LoginPreferences

class MainViewModel(private val loginPreferences: LoginPreferences) : ViewModel() {
    fun getAuth() : LiveData<Boolean> {
        return loginPreferences.getAuth().asLiveData()
    }
}

class MainVMFactory(private val loginPreferences: LoginPreferences) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(loginPreferences) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}