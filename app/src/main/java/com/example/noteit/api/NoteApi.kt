package com.example.noteit.api

import com.example.noteit.models.NoteRequest
import com.example.noteit.models.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteApi {

   @GET("/note")
   suspend fun getNotes():Response<List<NoteResponse>>

   @POST("/note")
   suspend fun createNote(@Body noteRequest: NoteRequest):Response<NoteResponse>

   @PUT("/note/{noteId}")
   suspend fun updateNote(@Path("noteId")noteId:String,@Body noteRequest: NoteRequest):Response<NoteResponse>

   @DELETE("/note/{noteId}")
   suspend fun deleteNote(@Path("noteId")noteId:String):Response<NoteResponse>


}