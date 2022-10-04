package com.example.noteapp.activities

import android.content.Intent
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import com.example.noteapp.R
import com.example.noteapp.databinding.LoginScreenBinding
import com.example.noteapp.models.User
import com.example.noteapp.services.FirebaseService
import com.example.noteapp.services.SharedPreferencesService

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginScreenBinding
    private val sharedPref = SharedPreferencesService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userLogged = sharedPref.getUserLogin(this@LoginActivity)
        if (!userLogged.isNullOrEmpty()) {
            signInSuccess(userLogged)
        }

        val firebaseService = FirebaseService()

        binding.loginButtonId.setOnClickListener {
            val user = User(binding.userId.text.toString(), binding.inputPasswordId.text.toString())
            firebaseService.signIn(user, ::signInSuccess, ::signInFailure)
        }

    }

    private fun signInSuccess(email: String) {
        sharedPref.addUserLogin(email, this@LoginActivity)
        val intent = Intent(this, NoteActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signInFailure() {
        showDialog(
            getString(R.string.dialog_error_auth_title),
            getString(R.string.dialog_error_auth_msg)
        )
    }

    private fun showDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.dialog_accept), null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}