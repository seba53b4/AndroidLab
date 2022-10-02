package com.example.noteapp.adapters

import com.example.noteapp.models.Note
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R

class ItemListAdapter(private val list: List<String>) : RecyclerView.Adapter<ItemListAdapter.ListItemViewHolder>(){

    class ListItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.list_item_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        return ListItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item

    }

    override fun getItemCount(): Int {
        return list.size
    }
}