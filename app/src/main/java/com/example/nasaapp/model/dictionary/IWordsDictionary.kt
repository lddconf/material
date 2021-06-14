package com.example.nasaapp.model.dictionary

interface IWordsDictionary {
    fun keyWords() : Set<String>
    fun addKeyWord(word : String) : Boolean
    fun removeKeyWord(word: String) : Boolean
}