package com.example.notesapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notesapp.api.UserAPI
import com.example.notesapp.models.UserRequest
import com.example.notesapp.models.UserResponse
import com.example.notesapp.utils.Constants.TAG
import com.example.notesapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Tag
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    private val _userResponseLiveData=MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData:LiveData<NetworkResult<UserResponse>>
    get() = _userResponseLiveData


    suspend fun registerUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response= userAPI.signup(userRequest)
        handleResponse(response)

    }
    suspend fun loginUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response=userAPI.signin(userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }


}