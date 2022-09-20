package com.example.noteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import com.example.noteapp.databinding.LoginScreenBinding

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
            val intent = Intent(this, NoteScreen::class.java)
            startActivity(intent)
            finish()
        }

    }
}