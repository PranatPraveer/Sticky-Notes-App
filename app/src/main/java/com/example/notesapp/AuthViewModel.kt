package com.example.notesapp

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.models.UserRequest
import com.example.notesapp.models.UserResponse
import com.example.notesapp.repository.UserRepository
import com.example.notesapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository):ViewModel() {


    val _userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.loginUser(userRequest)
        }
    }

    fun validateCredentials(
        username: String,
        emailAddress: String,
        password: String,
        isLogin:Boolean
    ): Pair<Boolean, String> {
        var result = Pair(true,"")
        if (!isLogin &&TextUtils.isEmpty(username) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(
                password
            )
        ) {
            result = Pair(false, "Please provide the credentials")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            result = Pair(false, "Please provide valid email")
        } else if (password.length<3){
            result= Pair(false,"Password Length should be greater than 3")
        }

        return result
    }
}