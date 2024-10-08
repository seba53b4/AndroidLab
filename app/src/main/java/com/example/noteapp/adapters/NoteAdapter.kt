package com.example.noteapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.models.Note
import com.example.noteapp.R
import com.example.noteapp.utils.getDateTime

class NoteAdapter(private val list: List<Note>, private val onClickEvent: OnClickListener): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    interface OnClickListener{
        fun onClick(item: Note){}
    }

    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.title_note)
        val date : TextView = itemView.findViewById(R.id.date_note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_note,parent,false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.date.text = getDateTime(item.date)
        holder.title.setOnClickListener{
            onClickEvent.onClick(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}