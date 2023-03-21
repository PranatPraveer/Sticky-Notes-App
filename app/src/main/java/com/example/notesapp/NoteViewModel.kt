package com.example.notesapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.notesapp.models.NoteRequest
import com.example.notesapp.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository:NoteRepository):ViewModel() {

    val notesLiveData get() = noteRepository.notesStateFlow
    val statusLiveData get() = noteRepository.statusStateFlow
    val dbLiveData get() = noteRepository.dbLiveData
    fun getNotes(){
        viewModelScope.launch {
            noteRepository.getNotes()
        }
    }
    fun createNotes(noteRequest: NoteRequest){
        viewModelScope.launch(Dispatchers.Main) {
            noteRepository.createNote(noteRequest)
        }
    }
    fun updateNotes(noteId:String, noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.updateNote(noteId,noteRequest)
        }
    }
    fun deleteNotes(noteId: String){
        viewModelScope.launch {
            noteRepository.deleteNote(noteId)
        }
    }
}