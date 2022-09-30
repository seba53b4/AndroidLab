package com.example.noteapp.services

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.noteapp.utils.Constants
import com.example.noteapp.R

class SharedPreferencesService {

    fun addUserLogin(email: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_preference_file_key),
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putBoolean(Constants.SharedPreferences.LOGGED_IN, true)
        editor.putString(Constants.SharedPreferences.EMAIL, email)
        editor.apply()
    }

    fun getUserLogin(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_preference_file_key),
            Context.MODE_PRIVATE
        )
        if (sharedPreferences.getBoolean(Constants.SharedPreferences.LOGGED_IN, false)) {
            return sharedPreferences.getString(Constants.SharedPreferences.EMAIL, null)
        }
        return null
    }

    fun removeUserLogin(context: Context) {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_preference_file_key),
            Context.MODE_PRIVATE
        )
        sharedPreferences.edit() {
            remove(Constants.SharedPreferences.LOGGED_IN)
            remove(Constants.SharedPreferences.EMAIL)
        }
    }
}