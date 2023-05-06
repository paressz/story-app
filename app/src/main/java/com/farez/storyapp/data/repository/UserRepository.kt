package com.farez.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.farez.storyapp.api.ApiService
import com.farez.storyapp.data.remote.response.LoginResponse
import com.farez.storyapp.data.remote.response.RegisterResponse
import com.farez.storyapp.data.remote.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log


class UserRepository (private val apiService: ApiService){

    private val loginResult = MutableLiveData<Result<LoginResponse>>()
    private val registerResult = MutableLiveData<Result<RegisterResponse>>()

    fun register ( name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> {
        registerResult.value = Result.Loading
        apiService
            .register(name, email, password)
            .enqueue(object: Callback<RegisterResponse>{
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        val info = response.body()
                        if (info != null) {
                            registerResult.value = Result.Success(info)
                        } else {
                            registerResult.value = Result.Error(REG_ERROR)
                        }
                    } else {
                        registerResult.value = Result.Error(REG_ERROR)
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    registerResult.value = Result.Error(REG_ERROR + ": ${t.message.toString()}")
                }

            })
        return registerResult
    }

    fun login (email: String, password: String) : LiveData<Result<LoginResponse>> {
        loginResult.value = Result.Loading
        apiService.login(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val info = response.body()
                        if (info != null) {
                            loginResult.value = Result.Success(info)
                        } else {
                            loginResult.value = Result.Error(LOGIN_ERROR)
                        }
                    } else {
                        loginResult.value = Result.Error(LOGIN_ERROR)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginResult.value = Result.Error(LOGIN_ERROR)
                }

            })
        return loginResult
    }

    companion object {
        const val REG_ERROR = "Failed to Register"
        const val LOGIN_ERROR = "Failed to Login"
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }

}