package com.farez.storyapp.ui.activity.login

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.farez.storyapp.data.local.preferences.LoginPreferences
import com.farez.storyapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository,private val loginPreferences: LoginPreferences) : ViewModel() {
    fun login(email : String, password : String) = userRepository.login(email, password)
    fun saveToken(token : String) {
        viewModelScope.launch(Dispatchers.IO) {
            loginPreferences.saveToken(token)
        }
    }
    fun setAuth(auth: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            loginPreferences.setAuth(auth)
        }
    }
    fun getAuth() : LiveData<Boolean>{
        return loginPreferences.getAuth().asLiveData()
    }
}

class LoginVMFactory(private val userRepository: UserRepository, private val loginPreferences: LoginPreferences) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository, loginPreferences) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}