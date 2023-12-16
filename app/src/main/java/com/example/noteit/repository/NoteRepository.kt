package com.example.noteit.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteit.api.NoteApi
import com.example.noteit.models.NoteRequest
import com.example.noteit.models.NoteResponse
import com.example.noteit.models.UserResponse
import com.example.noteit.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class NoteRepository @Inject constructor(val noteApi: NoteApi) {

    private val _noteLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val noteLiveData: LiveData<NetworkResult<List<NoteResponse>>>
        get() = _noteLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData


    suspend fun getNotes() {
        _noteLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.getNotes()

        if (response.isSuccessful && response.body() != null) {
            _noteLiveData.postValue(NetworkResult.Success(response.body()))

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _noteLiveData.postValue(NetworkResult.Error(null, errorObj.getString("message")))
        } else {
            _noteLiveData.postValue(NetworkResult.Error(null, "Something went Wrong"))
        }

    }

    suspend fun createNotes(noteRequest: NoteRequest) {
        _noteLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.createNote(noteRequest)

        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success("Note Created successfully"))
        } else if (response.errorBody() != null) {
            val error = JSONObject(response.errorBody()!!.charStream().readText())
            _statusLiveData.postValue(NetworkResult.Error(null, error.getString("message")))

        } else {
            _statusLiveData.postValue(NetworkResult.Error(null, "Something Went Wrong"))
        }

    }
    suspend fun updateNote(noteId:String,noteRequest: NoteRequest) {
        _noteLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.updateNote(noteId,noteRequest)

        if (response.isSuccessful && response.body() != null) {
            Log.i("Updated Note",response.body().toString())
            _statusLiveData.postValue(NetworkResult.Success("Note Updated successfully"))
        } else if (response.errorBody() != null) {
            val error = JSONObject(response.errorBody()!!.charStream().readText())
            Log.i("Error Updated Note",error.getString("message"))
            _statusLiveData.postValue(NetworkResult.Error(null, error.getString("message")))

        } else {
            _statusLiveData.postValue(NetworkResult.Error(null, "Something Went Wrong"))
        }

    }
    suspend fun deleteNote(noteId:String) {
        _noteLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.deleteNote(noteId)

        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success("Note deleted successfully"))
        } else if (response.errorBody() != null) {
            val error = JSONObject(response.errorBody()!!.charStream().readText())
            _statusLiveData.postValue(NetworkResult.Error(null, error.getString("message")))

        } else {
            _statusLiveData.postValue(NetworkResult.Error(null, "Something Went Wrong"))
        }

    }


}