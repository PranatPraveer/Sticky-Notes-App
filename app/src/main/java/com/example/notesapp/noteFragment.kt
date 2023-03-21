package com.example.notesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.notesapp.databinding.FragmentNoteBinding
import com.example.notesapp.models.NoteRequest
import com.example.notesapp.models.NoteResponse
import com.example.notesapp.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class noteFragment : Fragment() {

    private var _binding:FragmentNoteBinding?=null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private var note:NoteResponse?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentNoteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun bindObservers() {
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                noteViewModel.statusLiveData.collect{
                    when (it) {
                        is NetworkResult.Success -> {
                            findNavController().popBackStack()
                        }
                        is NetworkResult.Error -> {

                        }
                        is NetworkResult.Loading -> {

                        }
                    }
                }
            }
        }
    }

    private fun bindHandlers() {
        binding.btnDelete.setOnClickListener {
            note?.let {
                noteViewModel.deleteNotes(it!!._id)
            }
        }
                binding.btnSubmit.setOnClickListener {
                    val title = binding.txtTitle.text.toString()
                    val description = binding.txtDescription.text.toString()
                    val noteRequest = NoteRequest(description, title)
                    if (note == null) {
                        noteViewModel.createNotes(noteRequest)
                    } else {
                        noteViewModel.updateNotes(note!!._id, noteRequest)
                    }
                }
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("note")
        if(jsonNote !=null){
            note= Gson().fromJson(jsonNote,NoteResponse::class.java)
            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }
        }
        else{
            binding.addEditText.text= "Add Note"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}