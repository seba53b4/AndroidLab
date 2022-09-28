package com.example.noteapp

import android.content.Intent
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import com.example.noteapp.databinding.LoginScreenBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginScreenBinding
    private val sharedPref = SharedPreferencesService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userLogged = sharedPref.getUserLogin(this@LoginScreen)
        if (!userLogged.isNullOrEmpty()) {
            signInSuccess(userLogged)
        }

        binding.showPasswordButton.setOnClickListener {
            binding.inputPasswordId.inputType = if (binding.inputPasswordId.inputType === InputType.TYPE_TEXT_VARIATION_PASSWORD) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD else InputType.TYPE_TEXT_VARIATION_PASSWORD;
            binding.showPasswordButton.setImageResource(if (binding.inputPasswordId.inputType === InputType.TYPE_TEXT_VARIATION_PASSWORD) R.drawable.icons8_eye_64 else R.drawable.icons8_closed_eye_50)
        }

        val firebaseService = FirebaseService()

        binding.loginButtonId.setOnClickListener {
            val user = User(binding.userId.text.toString(), binding.inputPasswordId.text.toString())
            firebaseService.signIn(user, ::signInSuccess, ::signInFailure)
        }

    }

    private fun signInSuccess(email: String) {
        sharedPref.setUserLoginTrue(email, this@LoginScreen)
        val intent = Intent(this, NoteScreen::class.java)
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