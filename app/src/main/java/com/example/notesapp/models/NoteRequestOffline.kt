package com.example.notesapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteRequestOffline(
    val description: String,
    @PrimaryKey(autoGenerate = false)
    val title: String,
    val check: Boolean
)