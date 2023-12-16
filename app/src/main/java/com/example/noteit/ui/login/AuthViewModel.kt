package com.example.noteit.ui.login

import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteit.models.UserRequest
import com.example.noteit.models.UserResponse
import com.example.noteit.repository.UserRepository
import com.example.noteit.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    val userLiveData: LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userLiveData

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun validateResponse(
        username: String,
        email: String,
        password: String,
        isLogin: Boolean
    ): Pair<Boolean, String> {
        var result = Pair(true, "")
        if ((TextUtils.isEmpty(username) && !isLogin) || TextUtils.isEmpty(email) || TextUtils.isEmpty(
                password
            )
        ) {
            result = Pair(false, "Please Enter the Credentials")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result = Pair(false, "Please Enter a Valid Email Address")
        } else if (password.length <= 5) {
            result = Pair(false, "Please enter an password greater than 5 characters")
        }
        return result
    }
}
