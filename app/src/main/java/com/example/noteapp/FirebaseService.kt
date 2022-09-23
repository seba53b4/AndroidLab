package com.example.noteapp


import android.content.Context
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

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


    fun signOut() {
        Firebase.auth.signOut()
    }

    fun addNote(note: Note, success: () -> Unit, failure: () -> Unit) {
        val noteMap = hashMapOf(
            "emailUser" to note.emailUser,
            "title" to note.title,
            "body" to note.body,
            "itemList" to note.itemList,
            "date" to note.date,
            "isMultiline" to note.isMultiline
        )
        FirebaseFirestore.getInstance().collection(Constants.DB.NOTES).document(note.emailUser).set(noteMap)
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener {
                failure()
            }
    }

    fun modifyNote() {
    }

    fun deleteNote() {
    }


    fun getAllNotes(context: Context): MutableList<Note> {

        //TODO: el userLogged se podria obtener desde el activity y pasar por parametro a esta funcion
        val userLogged = SharedPreferencesService().getUserLogin(context)

        var noteList = mutableListOf<Note>()

        FirebaseFirestore.getInstance().collection(Constants.DB.NOTES).whereEqualTo("emailUser", userLogged)
            .get().addOnSuccessListener { notes ->
            for (note in notes) {
                var noteId = note.id
                var noteEmail = note.getString("emailUser") ?: ""
                var noteTitle = note.getString("title") ?: ""
                var noteBody = note.getString("body") ?: ""
                var noteItemList = note.get("itemList") as Array<String>
                var date = note.get("date")
                var isMultiline = note.getBoolean("isMultiline") ?: false
                noteList.add(
                    Note(
                        noteId, noteEmail, noteTitle, noteBody, noteItemList,
                        date as Timestamp, isMultiline
                    )
                )
            }
        }
        return noteList
    }

}