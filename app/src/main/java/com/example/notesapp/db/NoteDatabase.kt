package com.example.notesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notesapp.models.NoteResponse

@Database(entities = [NoteResponse::class], version = 1)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun getNoteDao():NoteDao
}