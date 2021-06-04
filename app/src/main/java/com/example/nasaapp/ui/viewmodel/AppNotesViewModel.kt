package com.example.nasaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.model.data.INotesRepo
import com.example.nasaapp.model.data.NasaAppNote

class AppNotesViewModel(private val repo: INotesRepo) : ViewModel() {
    private val viewState = MutableLiveData<List<NasaAppNote>>()
    private val repoNotes = repo.getNotes()

    private var recentDeletedNote: NasaAppNote? = null

    fun viewState() : LiveData<List<NasaAppNote>> = viewState

    fun deleteNote(uid: String) {
        recentDeletedNote = viewState.value?.filter { note ->
            note.uid == uid
        }?.firstOrNull()
        repo.deleteNote(uid)
    }

    fun undoLastDeletedNote() {
        recentDeletedNote?.let { note ->
            repo.saveNote(note)
        }
    }

    fun loadNotes() {
        viewState.value = repo.getNotes()
    }
}