package com.example.nasaapp.model.data

import androidx.lifecycle.LiveData

interface INotesRepo {
    fun getNotesSubscription(): LiveData<NoteResult>
    fun saveNote(note: NasaAppNote)
    fun deleteNote(uid: String) : Boolean
    fun getNoteById(uid: String): NasaAppNote?
}