package com.example.notesapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.databinding.FragmentMainBinding
import com.example.notesapp.models.NoteResponse
import com.example.notesapp.utils.NetworkResult
import com.example.notesapp.utils.NetworkUtils
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class mainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: NoteAdapter
    private val noteViewModel by viewModels<NoteViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAdapter= NoteAdapter(::onNoteClicked) 
        _binding=FragmentMainBinding.inflate(inflater,container,false)
        bindObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel.getNotes()
        binding.noteList.adapter= mAdapter
        binding.noteList.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }

    }

    private fun bindObservers() {
        if (NetworkUtils.isNetwokAvailable(requireContext(). applicationContext)) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                    noteViewModel.notesLiveData.collect{
                        binding.progressBar.isVisible = false
                        when (it) {
                            is NetworkResult.Success -> {
                                mAdapter.submitList(it.data)
                            }
                            is NetworkResult.Error -> {
                                mAdapter.submitList(it.data)
                                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }

                            is NetworkResult.Loading -> {
                                binding.progressBar.isVisible = true
                            }
                        }
                    }
                }
            }
        }else{
            noteViewModel.dbLiveData.observe(viewLifecycleOwner, Observer {
                binding.progressBar.isVisible = false
                mAdapter.submitList(it as MutableList<NoteResponse>)
            })
        }
    }

    private fun onNoteClicked(noteResponse:NoteResponse){
        val bundle=Bundle()
        bundle.putString("note",Gson().toJson(noteResponse))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment,bundle)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}