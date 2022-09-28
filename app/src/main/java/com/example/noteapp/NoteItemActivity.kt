package com.example.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.noteapp.databinding.ActivityNoteItemBinding
import com.example.noteapp.databinding.ActivityNoteScreenBinding

class NoteItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.getString("title").let { binding.textView.text = it }


    }
}