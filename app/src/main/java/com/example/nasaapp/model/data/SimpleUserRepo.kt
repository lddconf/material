package com.example.nasaapp.model.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object SimpleNotesRepo : INotesRepo {
    private val notes = mutableListOf<NasaAppNote>()
    private val liveData = MutableLiveData<NoteResult>()

    override fun getNotesSubscription(): LiveData<NoteResult> = liveData

    override fun saveNote(note: NasaAppNote) {
        val index = notes.indexOf(note)
        if (index < 0) {
            notes.add(note)
        } else {
            notes.removeAt(index)
            notes.add(index, note)
        }
        liveData.value = NoteResult.Success(notes)
    }

    override fun deleteNote(uid: String): Boolean {
        val note = notes.find {
            it.uid == uid
        }
        note?.let {
            notes.remove(note)
            liveData.value = NoteResult.Success(notes)
            return true
        }
        return false
    }

    override fun getNoteById(uid: String): NasaAppNote? = notes.find {
        it.uid == uid
    }

    private fun initDemoNotes() {
        for (i in 0..2) {
            notes.add(
                NasaAppNote(
                    title = "Title $i",
                    text = "Text $i",
                    color = NoteColor.GREEN
                )
            )
        }
        liveData.value = NoteResult.Success(notes)
    }

    init {
        initDemoNotes()
    }
}