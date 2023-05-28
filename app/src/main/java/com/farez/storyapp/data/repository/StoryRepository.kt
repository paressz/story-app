package com.farez.storyapp.data.repository

import android.util.Log
import com.farez.storyapp.data.remote.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.farez.storyapp.api.ApiService
import com.farez.storyapp.data.paging.StoryPagingSource
import com.farez.storyapp.data.remote.response.GetStoryResponse
import com.farez.storyapp.data.remote.response.NewStoryResponse
import com.farez.storyapp.data.remote.response.Story
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryRepository(private val apiService: ApiService) {
    val getStoriesResult = MutableLiveData<Result<List<Story>>>()
    val postStoryResult = MutableLiveData<Result<NewStoryResponse>>()
    fun testadapter(token : String) : LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(5, ),
            pagingSourceFactory = {StoryPagingSource(apiService, token)},

        ).liveData
    }
    fun getStories(token: String): LiveData<Result<List<Story>>> {
//        getStoriesResult.value = Result.Loading
//        apiService.getStory(token).enqueue(object : Callback<GetStoryResponse> {
//            override fun onResponse(
//                call: Call<GetStoryResponse>, response: Response<GetStoryResponse>
//            ) {
//                if (response.isSuccessful) {
//                    getStoriesResult.value = Result.Success(response.body()?.listStory as List<Story>)
//                } else {
//                    getStoriesResult.value = Result.Error("no response")
//                    Log.e("failed response", "onResponse: story info is null")
//                }
//            }
//
//            override fun onFailure(call: Call<GetStoryResponse>, t: Throwable) {
//                getStoriesResult.value = Result.Error(t.message.toString())
//                Log.e("failed response", "Failed: ${t.message.toString()}")
//            }
//
//        })
        return getStoriesResult
    }

    fun getMapStory(token: String) : LiveData<Result<List<Story>>> {
        getStoriesResult.value = Result.Loading
        apiService.getMapStory(token, 1).enqueue(object : Callback<GetStoryResponse> {
            override fun onResponse(
                call: Call<GetStoryResponse>,
                response: Response<GetStoryResponse>
            ) {
                if (response.isSuccessful) {
                    getStoriesResult.value = Result.Success(response.body()?.listStory as List<Story>)
                } else {
                    getStoriesResult.value = Result.Error("no response")
                    Log.e("failed response", "onResponse: story info is null")
                }
            }

            override fun onFailure(call: Call<GetStoryResponse>, t: Throwable) {
                getStoriesResult.value = Result.Error(t.message.toString())
                Log.e("failed response", "Failed: ${t.message.toString()}")
            }

        })

        return getStoriesResult
    }

    fun postStory(
        token: String, img: File, desccription: String
    ): LiveData<Result<NewStoryResponse>> {
        postStoryResult.postValue(Result.Loading)
        val textToMedida = "text/plain".toMediaType()
        val imgToMedia = "image/jpeg".toMediaTypeOrNull()
        val imgMultiPart: MultipartBody.Part =
            MultipartBody.Part.createFormData("photo", img.name, img.asRequestBody(imgToMedia))
        val descRequestBody = desccription.toRequestBody(textToMedida)
        apiService.newStory(token, imgMultiPart, descRequestBody)
            .enqueue(object : Callback<NewStoryResponse> {
                override fun onResponse(
                    call: Call<NewStoryResponse>, response: Response<NewStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val info = response.body()
                        if (info != null) {
                            postStoryResult.postValue(Result.Success(info))
                        } else {
                            postStoryResult.postValue(Result.Error(POST_ERROR))
                            Log.e("null info", "onResponse: post info null")
                        }
                    } else {
                        Log.e("no response", "onResponse: no Response")
                        postStoryResult.postValue(Result.Error(POST_ERROR))
                    }
                }

                override fun onFailure(call: Call<NewStoryResponse>, t: Throwable) {
                    Log.e("failure", "onResponse: ${t.message.toString()}")
                }
            })
        return postStoryResult
    }

    companion object {
        private const val POST_ERROR = "Failed to post story."

        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(apiService)
        }.also { instance = it }
    }
}