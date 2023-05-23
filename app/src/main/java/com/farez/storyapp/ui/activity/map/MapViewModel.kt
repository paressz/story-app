package com.farez.storyapp.ui.activity.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.farez.storyapp.data.repository.StoryRepository

class MapViewModel (private val storyRepository: StoryRepository) : ViewModel() {
    fun getMapStory(token : String) = storyRepository.getMapStory(token)
}

class MapVMFactory (private val storyRepository: StoryRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(storyRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}