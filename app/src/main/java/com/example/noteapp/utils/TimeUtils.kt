package com.example.noteapp.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

fun getDateTime(t: Timestamp): String? {
    try {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val dateToParse = t.toDate();
        return sdf.format(dateToParse)
    } catch (e: Exception) {
        return e.toString()
    }
}