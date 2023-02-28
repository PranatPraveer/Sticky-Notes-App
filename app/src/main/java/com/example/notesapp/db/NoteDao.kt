package com.example.notesapp.db

import androidx.room.*
import com.example.notesapp.models.NoteRequest
import com.example.notesapp.models.NoteResponse
import retrofit2.Response

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(noteResponse: List<NoteResponse>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNote(noteResponse: NoteResponse)

    @Query("DELETE FROM NoteResponse WHERE _id = :noteId")
    suspend fun deleteNote(noteId:String)

    @Query("SELECT * FROM NoteResponse")
    suspend fun getdb (): List<NoteResponse>

    @Update
    suspend fun updateNotes(noteResponse: NoteResponse)


}