package com.example.noteit.ui.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteit.databinding.NoteItemBinding
import com.example.noteit.models.NoteResponse

class NoteAdapter(private val onNoteClicked :(NoteResponse) ->Unit):ListAdapter<NoteResponse,NoteAdapter.MainViewHolder>(ComparatorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding =NoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val note =getItem(position)
        note?.let {
            holder.bindItems(it)
        }
    }

    inner class MainViewHolder(private val binding:NoteItemBinding):RecyclerView.ViewHolder(binding.root){
  fun bindItems(note:NoteResponse){
      binding.title.text =note.title
      binding.desc.text =note.description
      binding.root.setOnClickListener {
          onNoteClicked(note)
      }
  }


}

class ComparatorDiffUtil:DiffUtil.ItemCallback<NoteResponse>(){
    override fun areItemsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
        return oldItem._id==newItem._id
    }

    override fun areContentsTheSame(oldItem: NoteResponse, newItem: NoteResponse): Boolean {
       return oldItem==newItem
    }
}

}