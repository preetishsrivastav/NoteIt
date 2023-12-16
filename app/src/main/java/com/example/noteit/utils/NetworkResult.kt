package com.example.noteit.utils

sealed class NetworkResult<T>(val data:T? = null, val message:String? = null){

    class Success<T>(data: T?):NetworkResult<T>(data)
    class Error<T>(data: T? = null, message: String?):NetworkResult<T>(null,message)
    class Loading<T>():NetworkResult<T>()





}
