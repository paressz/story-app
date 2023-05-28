package com.farez.storyapp.ui.activity.Story

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.cachedIn
import com.farez.storyapp.data.local.preferences.LoginPreferences
import com.farez.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StoryViewModel(private val storyRepository: StoryRepository, private val loginPreferences: LoginPreferences) : ViewModel() {
    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            loginPreferences.delToken()
            loginPreferences.setAuth(false)
        }
    }
    fun getToken() : LiveData<String> = loginPreferences.getToken().asLiveData()
    fun getStories(token: String) = storyRepository.getStories(token)

    fun test(token: String) = storyRepository.testadapter(token).cachedIn(viewModelScope)
}

class StoryVMFactory (private val storyRepository: StoryRepository, private val loginPreferences: LoginPreferences) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(storyRepository, loginPreferences) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}