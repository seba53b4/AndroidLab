package com.example.noteapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class SharedPreferencesService {

    fun setUserLoginTrue(email: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_preference_file_key),
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putBoolean(LOGGED_IN, true)
        editor.putString(EMAIL, email)
        editor.apply()
    }

    fun getUserLogin(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.app_preference_file_key),
            Context.MODE_PRIVATE
        )
        if (sharedPreferences.getBoolean(LOGGED_IN, false)) {
            return sharedPreferences.getString(EMAIL, null)
        }
        return null
    }
}