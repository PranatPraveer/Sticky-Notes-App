package com.example.notesapp.api

import com.example.notesapp.models.NoteRequest
import com.example.notesapp.models.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NotesAPI {

    @GET("/notes")
    suspend fun getNotes():Response<List<NoteResponse>>

    @POST("/notes")
    suspend fun createNote(@Body noteRequest: NoteRequest):Response<NoteResponse>

    @PUT("/notes/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId:String, @Body noteRequest: NoteRequest):Response<NoteResponse>

    @DELETE("/notes/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId: String):Response<NoteResponse>
}