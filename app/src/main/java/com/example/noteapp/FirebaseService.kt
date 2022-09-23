package com.example.noteapp


import android.widget.Toast
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

}