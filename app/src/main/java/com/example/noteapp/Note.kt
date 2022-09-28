package com.example.noteapp

import com.google.firebase.Timestamp
import java.io.Serializable

class Note: Serializable {

    lateinit var id: String
    lateinit var emailUser: String
    lateinit var title: String
    lateinit var body: String
    var itemList: Array<String>? = null
    lateinit var date: Timestamp
    var isMultiline: Boolean = false

    constructor(id: String, emailUser: String, title: String, body: String, itemList: Array<String>?, date: Timestamp, isMultiline: Boolean) {
        this.id = id
        this.emailUser = emailUser
        this.title = title
        this.body = body
        this.itemList = itemList
        this.date = date
        this.isMultiline = isMultiline
    }

    constructor()

}
