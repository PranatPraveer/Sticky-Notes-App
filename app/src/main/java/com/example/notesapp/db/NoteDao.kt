package com.example.notesapp.db

import androidx.room.*
import com.example.notesapp.models.NoteRequest
import com.example.notesapp.models.NoteRequestOffline
import com.example.notesapp.models.NoteResponse
import retrofit2.Response

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(noteResponse: List<NoteResponse>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNote(noteResponse: NoteResponse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createNoteRequest(noteRequestOffline: NoteRequestOffline)

    @Query("DELETE FROM NoteResponse WHERE _id = :noteId")
    suspend fun deleteNote(noteId:String)

    @Delete
    suspend fun deleteNoteBackground(noteRequestOffline: NoteRequestOffline)

    @Query("SELECT * FROM NoteResponse")
    suspend fun getdb (): List<NoteResponse>

    @Query("SELECT * FROM NoteRequestOffline")
    suspend fun getofflinedb (): List<NoteRequestOffline>
    @Update
    suspend fun updateNotes(noteResponse: NoteResponse)

    @Update
    suspend fun updateOfflineNotes(noteRequestOffline: NoteRequestOffline)


}