package com.example.noteapp.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
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

        if (note.isMultiline) {
            val manager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
            recyclerView.layoutManager = manager;
            recyclerView.adapter = ItemListAdapter(note.itemList!!)
        }

        binding.deleteButton.setOnClickListener {
            showDialog(
                getString(R.string.dialog_note_delete_title),
                getString(R.string.dialog_confirm_action_title),
                confirmAdd, cancel)
        }

        binding.btnBackToNotes.setOnClickListener {
            backToNotes()
        }
    }

    private val confirmAdd = { dialog: DialogInterface, _: Int ->
        firebaseService.deleteNote(note,::deleteNoteSuccess,
            ::deleteNoteFailure)
    }

    private val confirm = { dialog: DialogInterface, _: Int ->
        backToNotes()
    }

    private val cancel = { dialog: DialogInterface, _: Int ->
        dialog.cancel()
    }

    private fun showDialog(
        title: String,
        message: String,
        listenerAccept: DialogInterface.OnClickListener?,
        listenerCancel: DialogInterface.OnClickListener?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.dialog_accept), listenerAccept)
        if (listenerCancel != null) {
            builder.setNegativeButton(getString(R.string.dialog_cancel), listenerCancel)
        }
        builder.show()
    }

    private fun backToNotes() {
        val intent = Intent(applicationContext, NoteActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun deleteNoteSuccess() {
        showDialog(
            getString(R.string.dialog_note_delete_ok_title),
            getString(R.string.dialog_note_delete_ok_msg),
            confirm, null
        )
    }

    private fun deleteNoteFailure() {
        showDialog(
            getString(R.string.dialog_note_delete_error_title),
            getString(R.string.dialog_note_save_error_msg),
            null, null
        )
    }
    private fun setNote() {
        val jSON= intent.extras?.getString("note")
        val gson = Gson()
        note = gson.fromJson(jSON,Note::class.java)
    }
}