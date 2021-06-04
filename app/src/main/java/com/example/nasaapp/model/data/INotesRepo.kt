package com.example.nasaapp.model.data

interface INotesRepo {
    fun getNotes(): List<NasaAppNote>
    fun saveNote(note: NasaAppNote)
    fun deleteNote(uid: String) : Boolean
    fun getNoteById(uid: String): NasaAppNote?
}