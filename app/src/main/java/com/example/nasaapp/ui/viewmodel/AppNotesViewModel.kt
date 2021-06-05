package com.example.nasaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.nasaapp.model.data.INotesRepo
import com.example.nasaapp.model.data.NasaAppNote
import com.example.nasaapp.model.data.NoteResult

class AppNotesViewModel(private val repo: INotesRepo) : ViewModel() {
    private val viewState = MutableLiveData<List<NasaAppNote>>()
    private val repoNotes = repo.getNotesSubscription()

    private var recentDeletedNote: NasaAppNote? = null

    private val observer = Observer<NoteResult> {
        it?.apply {
            when (this) {
                is NoteResult.Success<*> -> {
                    viewState.value = this.data as List<NasaAppNote>
                }
            }
        }
    }

    init {
        repoNotes.observeForever(observer)
    }

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

    override fun onCleared() {
        super.onCleared()
        repoNotes.removeObserver(observer)
    }
}