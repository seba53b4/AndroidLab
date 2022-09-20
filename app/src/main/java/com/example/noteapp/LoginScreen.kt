package com.example.noteapp

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import com.example.noteapp.databinding.LoginScreenBinding
import com.google.firebase.auth.FirebaseAuth

class LoginScreen : AppCompatActivity() {

    private lateinit var binding: LoginScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.showPasswordButton.setOnClickListener {
            binding.inputPasswordId.inputType = if (binding.inputPasswordId.inputType === InputType.TYPE_TEXT_VARIATION_PASSWORD)  InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD else InputType.TYPE_TEXT_VARIATION_PASSWORD;
            binding.showPasswordButton.setImageResource(if (binding.inputPasswordId.inputType === InputType.TYPE_TEXT_VARIATION_PASSWORD)  R.drawable.icons8_eye_64 else R.drawable.icons8_closed_eye_50)
        }

        binding.loginButtonId.setOnClickListener{

            signIn(binding.userId.text.toString(), binding.inputPasswordId.text.toString())

        }

    }

    private fun signIn(email: String, pass: String) {
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //TODO: invocar al metodo que lleve a las notas
                        Toast.makeText(this, "Login con éxito", Toast.LENGTH_SHORT).show()
                    } else {
                        showDialog("Error de autenticacion", "Se ha producido un error de autenticación")
                    }
                }
        }
    }

    private fun signUp(email: String, pass: String) {
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        showDialog("Registro exitoso", "Se ha registrado con éxito")
                        //TODO: invocar al metodo que lleve a las notas
                    } else {
                        showDialog("Error de registro", "Se ha producido un error al intentar registrar usuario")
                    }
                }
        }
    }

    fun showDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}