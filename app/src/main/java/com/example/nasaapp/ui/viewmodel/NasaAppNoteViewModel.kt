package com.example.nasaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.model.data.INotesRepo
import com.example.nasaapp.model.data.NasaAppNote
import kotlinx.coroutines.launch

class NasaAppNoteViewModel(private val repo: INotesRepo) : ViewModel() {
    private val viewState = MutableLiveData<NasaAppNote?>()

    private var currentNote: NasaAppNote? = null

    fun saveChanges(note: NasaAppNote?) {
        currentNote = note
    }

    fun viewState() : LiveData<NasaAppNote?> = viewState

    override fun onCleared() {
        currentNote?.let { note ->
            repo.saveNote(note)
        }
    }

    fun loadNote(uid: String) {
        viewState.value = repo.getNoteById(uid)
    }
}