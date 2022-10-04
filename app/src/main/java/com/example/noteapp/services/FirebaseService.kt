package com.example.noteapp.services


import android.content.Context
import com.example.noteapp.utils.Constants
import com.example.noteapp.models.Note
import com.example.noteapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class FirebaseService {

    fun signUp(user: User, success: () -> Unit, failure: () -> Unit) {
        if (user.email.isNotEmpty() && user.password.isNotEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        success()
                    } else {
                        failure()
                    }
                }
        }
    }


    fun signIn(user: User, success: (String) -> Unit, failure: () -> Unit) {
        if (user.email.isNotEmpty() && user.password.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        success(user.email)
                    } else {
                        failure()
                    }
                }
        }
    }

    fun deleteUser() {
            FirebaseAuth.getInstance().currentUser?.delete()
    }

    fun signOut() {
        Firebase.auth.signOut()
    }

    fun deleteAllNotesAndSignOut(email: String) {
        deleteAllNotes(email, ::signOut)
    }

    fun addNote(note: Note, success: () -> Unit, failure: () -> Unit) {
        val db = FirebaseFirestore.getInstance()

        val idNote = db.collection(Constants.DB.NOTES).document().id

        val noteMap = hashMapOf(
            Constants.DB.NOTE_ID to idNote,
            Constants.DB.NOTE_EMAIL to note.emailUser,
            Constants.DB.NOTE_TITLE to note.title,
            Constants.DB.NOTE_BODY to note.body,
            Constants.DB.NOTE_ITEM_LIST to note.itemList,
            Constants.DB.NOTE_DATE to note.date,
            Constants.DB.NOTE_IS_MULTILINE to note.isMultiline
        )

        db.collection(Constants.DB.NOTES).document(idNote).set(noteMap)
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener {
                failure()
            }
    }

    fun updateNote(note: Note, success: () -> Unit, failure: () -> Unit) {
        val noteRef =
            FirebaseFirestore.getInstance().collection(Constants.DB.NOTES).document(note.id)
        noteRef.update(Constants.DB.NOTE_TITLE, note.title)
        noteRef.update(Constants.DB.NOTE_BODY, note.body)
        noteRef.update(Constants.DB.NOTE_ITEM_LIST, note.itemList)
        noteRef.update(Constants.DB.NOTE_DATE, note.date)
        noteRef.update(Constants.DB.NOTE_IS_MULTILINE, note.isMultiline)
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener {
                failure()
            }
    }

    fun deleteNote(note: Note, success: () -> Unit, failure: () -> Unit) {
        FirebaseFirestore.getInstance().collection(Constants.DB.NOTES).document(note.id).delete()
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener {
                failure()
            }
    }

    fun deleteAllNotes(context: Context) {
        val userLogged = SharedPreferencesService().getUserLogin(context) ?: ""
        deleteAllNotes(userLogged, null)
    }

    fun deleteAllNotes(userLogged: String, success: (() -> Unit)?) {

        val db = FirebaseFirestore.getInstance()

        db.collection(Constants.DB.NOTES)
            .whereEqualTo(Constants.DB.NOTE_EMAIL, userLogged)
            .get().addOnSuccessListener { notes ->
                for (note in notes) {
                    db.collection(Constants.DB.NOTES).document(note.id).delete()
                }
                if (success != null) {
                    success()
                }
            }
    }

    fun deleteAllNotesByList(notes: MutableList<Note>) {
        val db = FirebaseFirestore.getInstance()
        notes.forEach {
            db.collection(Constants.DB.NOTES).document(it.id).delete()
        }
    }

    suspend fun getAllNotes(context: Context): MutableList<Note> {
        val userLogged = SharedPreferencesService().getUserLogin(context) ?: ""
        return getAllNotes(userLogged)
    }

    suspend fun getAllNotes(userLogged: String): MutableList<Note> {
        var noteList = mutableListOf<Note>()

        return try {

            FirebaseFirestore.getInstance().collection(Constants.DB.NOTES)
                .whereEqualTo(Constants.DB.NOTE_EMAIL, userLogged)
                .get().addOnSuccessListener { notes ->
                    for (note in notes) {
                        noteList.add(note.toObject(Note::class.java))
                    }
                }.await()

            withContext(Dispatchers.Main) {
                return@withContext noteList
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                return@withContext noteList
            }
        }
    }

}