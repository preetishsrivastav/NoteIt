package com.example.noteit.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteit.models.NoteRequest
import com.example.noteit.models.NoteResponse
import com.example.noteit.repository.NoteRepository
import com.example.noteit.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository):ViewModel() {

    val notesLiveData:LiveData<NetworkResult<List<NoteResponse>>>
        get() = repository.noteLiveData

    val statusLiveData:LiveData<NetworkResult<String>>
        get() = repository.statusLiveData

    fun getNotes(){
        viewModelScope.launch {
            repository.getNotes()
        }
    }

    fun createNote(noteRequest: NoteRequest){
        viewModelScope.launch {
            repository.createNotes(noteRequest)
        }
    }

    fun updateNotes(noteId:String,noteRequest: NoteRequest){
        viewModelScope.launch {
            repository.updateNote(noteId,noteRequest)
        }
    }

    fun deleteNote(noteId: String){
        viewModelScope.launch {
            repository.deleteNote(noteId)
        }
    }
}