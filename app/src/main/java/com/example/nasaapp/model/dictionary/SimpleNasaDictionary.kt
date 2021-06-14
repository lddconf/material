package com.example.nasaapp.model.dictionary

class SimpleNasaDictionary : IWordsDictionary {
    private val keywords = mutableSetOf<String>("Space", "Moon", "Milky Way", "Earth")

    override fun keyWords(): Set<String> = keywords

    override fun addKeyWord(word: String): Boolean = keywords.add(word)

    override fun removeKeyWord(word: String): Boolean = keywords.remove(word)
}