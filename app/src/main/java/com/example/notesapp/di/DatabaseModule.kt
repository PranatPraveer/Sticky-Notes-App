package com.example.notesapp.di

import android.content.Context
import androidx.room.Room
import com.example.notesapp.db.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideNoteDB(@ApplicationContext context: Context):NoteDatabase{
        return Room.databaseBuilder(context,NoteDatabase::class.java,"NoteDatabase").build()
    }
}