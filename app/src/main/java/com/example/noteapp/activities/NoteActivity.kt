package com.example.noteapp.activities


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.databinding.ActivityNoteScreenBinding
import com.example.noteapp.models.Note
import com.example.noteapp.models.User
import com.example.noteapp.services.FirebaseService
import com.example.noteapp.services.SharedPreferencesService
import com.google.firebase.Timestamp
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteScreenBinding
    lateinit var recyclerView: RecyclerView
    private val sharedPref = SharedPreferencesService()
    private val firebaseService = FirebaseService()
    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.recyclerviewNotes

        userEmail = sharedPref.getUserLogin(this@NoteActivity) ?: ""

        val manager =  GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.layoutManager = manager;

        CoroutineScope(Dispatchers.Main).launch {

            val notes = firebaseService.getAllNotes(userEmail)
            recyclerView.adapter = NoteAdapter(notes, object:NoteAdapter.OnClickListener{
                override fun onClick(item: Note) {
                    val intent = Intent(applicationContext, NoteItemActivity::class.java)
                    val gson = Gson()
                    intent.putExtra("note", gson.toJson(item))
                    startActivity(intent)
                }
            })
        }

        binding.logoutBtn.setOnClickListener{
            // desloguearse
            showDialog(getString(R.string.dialog_logout_title), getString(R.string.dialog_logout_msg),confirmLogOut)
        }

        binding.fabAddNote.setOnClickListener{
            val intent = Intent(applicationContext, NoteAddActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.garbageButton.setOnClickListener {
            showDialog(getString(R.string.dialog_delete_account_title), getString(R.string.dialog_delete_account_msg),confirmDeleteAccount)
        }
    }

    private val confirmDeleteAccount = { dialog: DialogInterface, _: Int ->
        firebaseService.deleteAllNotes(userEmail,null)
        firebaseService.deleteUser()
        backToLogin()
    }

    private fun backToLogin() {
        sharedPref.removeUserLogin(this@NoteActivity)
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showDialog(title: String, message: String, confirmFun :(DialogInterface,Int) -> Unit ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.dialog_accept), confirmFun)
        builder.setNegativeButton(getString(R.string.dialog_cancel), cancelLogOut)
        builder.show()
    }

    private val confirmLogOut = { dialog: DialogInterface, _: Int ->
        firebaseService.signOut()
        backToLogin()
    }

    private val cancelLogOut = { dialog: DialogInterface, _: Int ->
        dialog.cancel()
    }


}