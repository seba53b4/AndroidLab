package com.example.noteapp.activities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.allViews
import androidx.core.view.get
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityNoteAddBinding
import com.example.noteapp.models.Note
import com.example.noteapp.services.FirebaseService
import com.example.noteapp.services.SharedPreferencesService
import com.google.firebase.Timestamp
import com.google.gson.Gson


class NoteAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteAddBinding
    private val sharedPref = SharedPreferencesService()
    private val firebaseService = FirebaseService()
    private lateinit var userEmail: String
    private var isEdit: Boolean = false
    private lateinit var note : Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userEmail = sharedPref.getUserLogin(this@NoteAddActivity) ?: ""



        binding.btnBackToNotes.setOnClickListener {
            if (noteEmpty()) {
                backToNotes()
            } else {
                showDialog(
                    getString(R.string.dialog_note_back_title),
                    getString(R.string.dialog_note_back_msg),
                    confirm, cancel)
            }
        }

        binding.btnSaveNewNote.setOnClickListener {

            if (!noteEmpty()) {
                if (isEdit) {
                    var noteCreated = createNote()
                    noteCreated.id = note.id
                    note = noteCreated
                    firebaseService.updateNote(
                        noteCreated,
                        ::editNoteSuccess,
                        ::addNoteFailure)
                } else {
                    firebaseService.addNote(
                        createNote(),
                        ::addNoteSuccess,
                        ::addNoteFailure
                    )
                }
            }
        }

        binding.btnAddItem.setOnClickListener {
            addItem()
        }

        isEdit = intent.extras?.getBoolean("isEdit") == true
        if (isEdit) {
            setNote()
            binding.editTextTitle.setText(note.title)
            binding.editTextBody.setText(note.body)
            note.itemList?.let { addItemListOfNote(it) }
            binding.materialToolbar.title = "Editar Nota"
        }

    }

    private fun addItem() {
        val inflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.item_note, null)
        binding.noteItems!!.addView(rowView, binding.noteItems!!.childCount)
    }

    private fun addItemListOfNote( list: List<String>){
        list.forEach{
            addItem(it)
        }
    }

    private fun addItem(item : String) {
        val inflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.item_note, null)
        rowView.findViewById<EditText>(R.id.editText_item).setText(item)
        binding.noteItems!!.addView(rowView, binding.noteItems!!.childCount)
    }
    fun onDelete(view: View) {
        binding.noteItems!!.removeView(view.parent as View)
    }

    private fun noteEmpty(): Boolean {
        return binding.editTextTitle.text.isNullOrEmpty() && binding.editTextBody.text.isNullOrEmpty() && getItemList().isEmpty()
    }

    private fun createNote(): Note {
        val title = binding.editTextTitle.text.toString()
        val body = binding.editTextBody.text.toString()
        var itemList = getItemList()
        val isMultiline = itemList.isNotEmpty()

        return Note("", userEmail, title, body, itemList, Timestamp.now(), isMultiline)
    }

    private fun getItemList(): List<String> {
        var itemList = mutableListOf<String>()
        binding.noteItems!!.allViews.forEach {
            if (it is EditText && !it.text.isNullOrEmpty()) {
                itemList.add(it.text.toString())
            }
        }
        return itemList
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

    private val confirm = { dialog: DialogInterface, _: Int ->
        backToNotes()
    }

    private val cancel = { dialog: DialogInterface, _: Int ->
        dialog.cancel()
    }

    private fun editNoteSuccess(){
        showDialog(
            getString(R.string.dialog_note_save_ok_title),
            getString(R.string.dialog_note_save_ok_msg),
            confirmEdit, null
        )
    }

    private val confirmEdit = { dialog: DialogInterface, _: Int ->
        backToNoteEdited()
    }

    private fun addNoteSuccess() {
        showDialog(
            getString(R.string.dialog_note_save_ok_title),
            getString(R.string.dialog_note_save_ok_msg),
            confirm, null
        )
    }

    private fun addNoteFailure() {
        showDialog(
            getString(R.string.dialog_note_save_error_title),
            getString(R.string.dialog_note_save_error_msg),
            null, null
        )
    }

    private fun backToNoteEdited() {
        val intent = Intent(applicationContext, NoteItemActivity::class.java)
        val gson = Gson()
        intent.putExtra("note", gson.toJson(note))
        startActivity(intent)
        finish()
    }

    private fun backToNotes() {
        val intent = Intent(applicationContext, NoteActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setNote() {
        val jSON= intent.extras?.getString("note")
        val gson = Gson()
        note = gson.fromJson(jSON,Note::class.java)
    }



}