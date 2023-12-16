package com.example.noteit.ui.note

import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteit.R
import com.example.noteit.api.NoteApi
import com.example.noteit.databinding.FragmentMainBinding
import com.example.noteit.models.NoteResponse
import com.example.noteit.utils.Helper
import com.example.noteit.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? =null
    private val binding
        get() = _binding
    private val noteViewModel by viewModels<NoteViewModel>()
     lateinit var adapter: NoteAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentMainBinding.inflate(layoutInflater,container,false)
       adapter = NoteAdapter(::onNoteClicked)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindObserver()
        noteViewModel.getNotes()
        binding!!.noteList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding!!.noteList.adapter =adapter
        binding!!.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }
    }

    fun onNoteClicked(note:NoteResponse){
        val bundle =Bundle()
        bundle.putString("Note",Gson().toJson(note))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment,bundle)
    }

    private fun bindObserver() {
        noteViewModel.notesLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    Helper.dismissProgressBar()
                     adapter.submitList(it.data)
                }

                is NetworkResult.Error -> {
                    Helper.dismissProgressBar()
                    showValidationError(it.message.toString())
                }

                is NetworkResult.Loading -> {
                   Helper.showCustomProgressBar(requireActivity())
                }

            }
        }
    }
    private fun showValidationError(error:String) {
        Toast.makeText(requireActivity(),error,Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}