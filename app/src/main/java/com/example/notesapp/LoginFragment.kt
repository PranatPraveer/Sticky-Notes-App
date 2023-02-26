package com.example.notesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.notesapp.databinding.FragmentLoginBinding
import com.example.notesapp.models.UserRequest
import com.example.notesapp.utils.Helper
import com.example.notesapp.utils.NetworkResult
import com.example.notesapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding:FragmentLoginBinding? =null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentLoginBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            Helper.hideKeyboard(it)
            val validationResult = validateUserInput()
            if (validationResult.first) {
                val userRequest = getUserRequest()
                authViewModel.loginUser(userRequest)
            } else {
                showValidationErrors(validationResult.second)
            }
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }
        bindObserver()
    }

    private fun showValidationErrors(error: String) {
        binding.txtError.text = String.format(resources.getString(R.string.txt_error_message, error))
    }
        private fun getUserRequest(): UserRequest {
            return binding.run {
                UserRequest(
                    txtEmail.text.toString(),
                    txtPassword.text.toString(),
                    ""
                )
            }
        }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest=getUserRequest()

        return authViewModel.validateCredentials("", userRequest.email,userRequest.password,true)

    }
    private fun bindObserver() {
        authViewModel._userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    showValidationErrors(it.message.toString())
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}