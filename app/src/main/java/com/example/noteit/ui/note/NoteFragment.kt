package com.example.noteit.ui.note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.noteit.R
import com.example.noteit.databinding.FragmentNoteBinding
import com.example.noteit.models.NoteRequest
import com.example.noteit.models.NoteResponse
import com.example.noteit.utils.Helper
import com.example.noteit.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var note: NoteResponse? = null
    private val noteViewModel by viewModels<NoteViewModel>()
    private var _binding: FragmentNoteBinding? = null
    private val binding
        get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(layoutInflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun bindObservers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    Helper.dismissProgressBar()
                    findNavController().popBackStack()
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

    fun showValidationError(error: String) {
        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
    }


    private fun bindHandlers() {
        binding!!.btnDelete.setOnClickListener {
            note?.let {
                noteViewModel.deleteNote(it._id)
            }
        }
        binding!!.btnSubmit.setOnClickListener {
            val title: String = binding!!.txtTitle.text.toString()
            val description: String = binding!!.txtDescription.text.toString()
            val noteRequest = NoteRequest(title, description)
            if (note == null) {
                noteViewModel.createNote(noteRequest)
            } else {
                noteViewModel.updateNotes(note!!._id, noteRequest)
                Log.i("noteId",note!!._id)
            }
        }
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("Note")
        if (jsonNote != null) {
            note = Gson().fromJson(jsonNote, NoteResponse::class.java)
            note?.let {
                binding!!.txtTitle.setText(it.title)
                binding!!.txtDescription.setText(it.description)
            }
        } else {
            binding!!.addEditText.text = resources.getString(R.string.add_note)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}