package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.noteapp.databinding.ActivityNoteScreenBinding

class NoteScreen : AppCompatActivity() {

    private lateinit var binding: ActivityNoteScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}