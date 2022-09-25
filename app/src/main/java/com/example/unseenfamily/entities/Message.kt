package com.example.unseenfamily.entities

import java.util.*

class Message (val from: String, val content: String, val timestamp: String) {
    constructor(content: String): this("user", content, Date().toString())
}