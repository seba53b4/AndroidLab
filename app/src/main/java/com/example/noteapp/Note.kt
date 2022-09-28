package com.example.noteapp

import com.google.firebase.Timestamp

data class Note(val id: String, val emailUser: String, val title:String, val body: String, val itemList: Array<String>?, val date: Timestamp, val isMultiline: Boolean)
