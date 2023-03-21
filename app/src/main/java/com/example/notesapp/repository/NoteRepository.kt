package com.example.notesapp.repository

import android.content.Context
import android.widget.Toast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation

import com.example.notesapp.api.NotesAPI
import com.example.notesapp.db.NoteDatabase
import com.example.notesapp.models.NoteRequest
import com.example.notesapp.models.NoteRequestOffline
import com.example.notesapp.models.NoteResponse
import com.example.notesapp.utils.NetworkResult
import com.example.notesapp.utils.NetworkUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val notesAPI: NotesAPI,private val noteDatabase: NoteDatabase,@ApplicationContext private val applicationContext:Context) {

    private val _notesStateFlow= MutableStateFlow<NetworkResult<List<NoteResponse>>>(NetworkResult.Loading())
    val notesStateFlow: StateFlow<NetworkResult<List<NoteResponse>>>
    get() = _notesStateFlow

    val _statusStateFlow = MutableStateFlow<NetworkResult<String>>(NetworkResult.Loading())
    val statusStateFlow:StateFlow<NetworkResult<String>>
    get() = _statusStateFlow

    val _dbLiveData=MutableLiveData<List<NoteResponse>>()
    val dbLiveData:LiveData<List<NoteResponse>>
    get() = _dbLiveData

    suspend fun getNotes(){
        if (NetworkUtils.isNetwokAvailable(applicationContext)) {
            val response = notesAPI.getNotes()
            if (response.isSuccessful && response.body() != null) {
                noteDatabase.getNoteDao().addNote(response.body()!!)
                _notesStateFlow.emit(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _notesStateFlow.emit(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _notesStateFlow.emit(NetworkResult.Error("Something Went Wrong"))
            }
        }
        else{
            val response=noteDatabase.getNoteDao().getdb()
            _dbLiveData.postValue(response)
            Toast.makeText(applicationContext,"Network Not Available",Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun createNote(noteRequest: NoteRequest){
        if(NetworkUtils.isNetwokAvailable(applicationContext)) {
            val response = notesAPI.createNote(noteRequest)
            noteDatabase.getNoteDao().createNote(response.body()!!)
            handleResponse(response, "Note Created")
        }
        else{
            Toast.makeText(applicationContext,"Network Unavailable will do it later",Toast.LENGTH_SHORT).show()
            noteDatabase.getNoteDao().createNoteRequest(NoteRequestOffline(noteRequest.description,noteRequest.title,false))
            _statusStateFlow.emit(NetworkResult.Success("Note Created"))
        }
    }
    suspend fun deleteNote(noteId: String){
        if(NetworkUtils.isNetwokAvailable(applicationContext)) {
            val response = notesAPI.deleteNote(noteId)
            noteDatabase.getNoteDao().deleteNote(noteId)
            handleResponse(response, "Note Deleted")
        }
        else{
            Toast.makeText(applicationContext,"Network Not Available Unable to perform Action",Toast.LENGTH_SHORT).show()
        }
    }
    suspend fun updateNote(noteId:String, noteRequest: NoteRequest){
        if(NetworkUtils.isNetwokAvailable(applicationContext)) {
            val response = notesAPI.updateNote(noteId, noteRequest)
            handleResponse(response,"Note Updated")
        }
        else{
            Toast.makeText(applicationContext,"Network Not Available Unable to perform Action",Toast.LENGTH_SHORT).show()
        }
    }
    private suspend fun handleResponse(response: Response<NoteResponse>, message:String) {
        if (response.isSuccessful && response.body() != null) {
            _statusStateFlow.emit(NetworkResult.Success("Note Created"))
        } else {
            _statusStateFlow.emit(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun createNotesBackground(){
        val notes=noteDatabase.getNoteDao().getofflinedb()
        notes.forEach{
            if(it.check!=true) {
                val response = notesAPI.createNote(NoteRequest(it.description, it.title))
                if (response.body() != null && response.isSuccessful) {
                    noteDatabase.getNoteDao()
                        .updateOfflineNotes(NoteRequestOffline(it.description, it.title, true))
                }
            }
            else{
                noteDatabase.getNoteDao().deleteNoteBackground(it)
            }
        }
    }


}