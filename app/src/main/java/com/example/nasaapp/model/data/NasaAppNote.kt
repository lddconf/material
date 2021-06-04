package com.example.nasaapp.model.data

import java.util.*

data class NasaAppNote(
    val title: String = "",
    val text: String = "",
    val uid: String = UUID.randomUUID().toString(),
    val lastChanged: Date = Date(),
    var color: NoteColor = NoteColor.RED
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NasaAppNote
        return this.uid == other.uid
    }

    override fun hashCode(): Int {
        return uid.hashCode()
    }
}