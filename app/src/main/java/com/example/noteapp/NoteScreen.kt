package com.example.noteapp


import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.databinding.ActivityNoteScreenBinding
import com.google.firebase.Timestamp
import java.util.*
import com.example.noteapp.R


class NoteScreen : AppCompatActivity() {

    private lateinit var binding: ActivityNoteScreenBinding
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.recyclerviewNotes

        val notes = mutableListOf<Note>()
        notes.add(Note("1","a@mail.com","Lista Super","Comprar pan", null, Timestamp.now(),false));
        notes.add(Note("1","a@mail.com","Lista Recetas","Comprar tacos", null, Timestamp.now(),false));
        notes.add(Note("1","a@mail.com","Lista Verduleria","Comprar lechuga", null, Timestamp.now(),false));
        notes.add(Note("1","a@mail.com","Lista Verduleria","Comprar lechuga", null, Timestamp.now(),false));
        notes.add(Note("1","a@mail.com","Lista Verduleria","Comprar lechuga", null, Timestamp.now(),false));
        notes.add(Note("1","a@mail.com","Lista Verduleria","Comprar lechuga", null, Timestamp.now(),false));
        notes.add(Note("1","a@mail.com","Lista Verduleria","Comprar lechuga", null, Timestamp.now(),false));
        notes.add(Note("1","a@mail.com","Lista Verduleria","Comprar lechuga", null, Timestamp.now(),false));
        notes.add(Note("1","a@mail.com","Lista Verduleria","Comprar lechuga", null, Timestamp.now(),false));
        notes.add(Note("1","a@mail.com","Lista Verduleria","Comprar lechuga", null, Timestamp.now(),false));
        notes.add(Note("1","a@mail.com","Lista Verduleria","Comprar lechuga", null, Timestamp.now(),false));

        val manager =  GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.layoutManager = manager;
        recyclerView.adapter = NoteAdapter(notes)


    }
}