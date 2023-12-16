package com.example.noteit.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteit.api.UserApi
import com.example.noteit.models.UserRequest
import com.example.noteit.models.UserResponse
import com.example.noteit.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class UserRepository @Inject constructor(val userApi: UserApi) {

    private val _userMutableLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userMutableLiveData


    suspend fun registerUser(userRequest: UserRequest) {
        _userMutableLiveData.postValue(NetworkResult.Loading())
        val result = userApi.signUp(userRequest)

        if (result.isSuccessful && result.body() != null) {
            _userMutableLiveData.postValue(NetworkResult.Success(result.body()!!))
        } else if (result.errorBody() != null) {
            val errorObj = JSONObject(result.errorBody()!!.charStream().readText())
            _userMutableLiveData.postValue(NetworkResult.Error(null, errorObj.getString("message")))
        } else {
            _userMutableLiveData.postValue(NetworkResult.Error(null, "Something went wrong"))
        }

    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userMutableLiveData.postValue(NetworkResult.Loading())
        val result = userApi.signIn(userRequest)
        if (result.isSuccessful && result.body() != null) {
            _userMutableLiveData.postValue(NetworkResult.Success(result.body()!!))
        } else if (result.errorBody() != null) {
            val errorObj = JSONObject(result.errorBody()!!.charStream().readText())
            _userMutableLiveData.postValue(NetworkResult.Error(null, errorObj.getString("message")))
        } else {
            _userMutableLiveData.postValue(NetworkResult.Error(null, "Something Went wrong"))
        }
    }
}