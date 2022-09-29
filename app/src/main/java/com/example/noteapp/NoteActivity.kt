package com.example.noteapp


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.adapters.NoteAdapter
import com.example.noteapp.databinding.ActivityNoteScreenBinding
import com.google.firebase.Timestamp


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

        val notes = mutableListOf<Note>()
        notes.add(Note("1","a@mail.com","Lista Super de cosas para ver si se rompe","Comprar pan", null, Timestamp.now(),false));
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
        recyclerView.adapter = NoteAdapter(notes, object:NoteAdapter.OnClickListener{
            override fun onClick(item: Note) {
                val intent = Intent(applicationContext,NoteItemActivity::class.java)
                intent.putExtra("title",item.title)
                startActivity(intent)
            }
        })

        binding.logoutBtn.setOnClickListener{
            // desloguearse
            showDialog(getString(R.string.dialog_logout_title), getString(R.string.dialog_logout_msg))
        }


    }

    private fun showDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.dialog_accept), confirmLogOut)
        builder.setNegativeButton(getString(R.string.dialog_cancel), cancelLogOut)
        builder.show()
    }

    private val confirmLogOut = { dialog: DialogInterface, _: Int ->
        firebaseService.deleteAllNotes(userEmail)
        sharedPref.removeUserLogin(this@NoteActivity)
        val intent = Intent(applicationContext,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private val cancelLogOut = { dialog: DialogInterface, _: Int ->
        dialog.cancel()
    }


}