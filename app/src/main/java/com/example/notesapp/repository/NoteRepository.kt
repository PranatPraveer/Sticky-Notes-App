package com.example.notesapp.repository

import android.content.Context
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.notesapp.api.NotesAPI
import com.example.notesapp.db.NoteDatabase
import com.example.notesapp.models.NoteRequest
import com.example.notesapp.models.NoteResponse
import com.example.notesapp.noteFragment
import com.example.notesapp.utils.NetworkResult
import com.example.notesapp.utils.NetworkUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val notesAPI: NotesAPI,private val noteDatabase: NoteDatabase,@ApplicationContext private val applicationContext:Context) {

    private val _notesLiveData=MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val notesLiveData:LiveData<NetworkResult<List<NoteResponse>>>
    get() = _notesLiveData

    val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData:LiveData<NetworkResult<String>>
    get() = _statusLiveData

    val _dbLiveData=MutableLiveData<List<NoteResponse>>()
    val dbLiveData:LiveData<List<NoteResponse>>
    get() = _dbLiveData

    suspend fun getNotes(){

        if (NetworkUtils.isNetwokAvailable(applicationContext)) {
            _notesLiveData.postValue(NetworkResult.Loading())
            val response = notesAPI.getNotes()
            if (response.isSuccessful && response.body() != null) {
                noteDatabase.getNoteDao().addNote(response.body()!!)
                _notesLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _notesLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _notesLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
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
            _statusLiveData.postValue(NetworkResult.Loading())
            val response = notesAPI.createNote(noteRequest)
            noteDatabase.getNoteDao().createNote(response.body()!!)
            handleResponse(response, "Note Created")
        }
        else{
            Toast.makeText(applicationContext,"Network Not Available Unable to perform Action",Toast.LENGTH_SHORT).show()
        }
    }
    suspend fun deleteNote(noteId: String){
        if(NetworkUtils.isNetwokAvailable(applicationContext)) {
            _statusLiveData.postValue(NetworkResult.Loading())
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
            _statusLiveData.postValue(NetworkResult.Loading())
            val response = notesAPI.updateNote(noteId, noteRequest)
            handleResponse(response, "Note Updated")
        }
        else{
            Toast.makeText(applicationContext,"Network Not Available Unable to perform Action",Toast.LENGTH_SHORT).show()

        }
    }
    private fun handleResponse(response: Response<NoteResponse>, message:String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success("Note Created"))
        } else {
            _statusLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
}