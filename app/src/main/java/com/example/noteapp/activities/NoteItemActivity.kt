package com.example.noteapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.adapters.ItemListAdapter
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.databinding.ActivityNoteItemBinding

import com.example.noteapp.models.Note
import com.example.noteapp.services.FirebaseService
import com.example.noteapp.services.SharedPreferencesService
import com.example.noteapp.utils.getDateTime
import com.google.gson.Gson

class NoteItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteItemBinding
    private lateinit var recyclerView: RecyclerView
    private val sharedPref = SharedPreferencesService()
    private val firebaseService = FirebaseService()
    private lateinit var note : Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.listItemRecyclerView

        setNote()
        binding.titleTextView.text = note.title
        binding.descriptionTextView.text = note.body
        binding.dateTextView.text = getDateTime(note.date)

        val manager =  GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.layoutManager = manager;
        recyclerView.adapter = ItemListAdapter(note.itemList!!)
    }

    private fun setNote(){
        val str = intent.extras?.getString("note")
        val gson = Gson()
        note = gson.fromJson(str,Note::class.java)
    }
}