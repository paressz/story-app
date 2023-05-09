package com.farez.storyapp.ui.activity.Upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.farez.storyapp.data.repository.StoryRepository
import java.io.File

class UploadViewModel (private val storyRepository: StoryRepository) : ViewModel()  {
    fun newStory(token : String, imgFile : File, desc : String) = storyRepository.postStory(token, imgFile, desc)

}

class UploadVMFactory (private val storyRepository: StoryRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(UploadViewModel::class.java)) {
            return UploadViewModel(storyRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}