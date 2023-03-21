package com.example.notesapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteResponse(
    val __v: Int,
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    val createdAt: String,
    val description: String,
    val title: String,
    val updatedAt: String,
    val userId: String
)