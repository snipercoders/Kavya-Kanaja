package com.example.kavyakanaja

data class DifficultWord(
    val word: String,
    val meaning: String
)

data class Poem(
    val id: Int,
    val title: String,
    val poet: String,
    val poetEnglish: String = "",
    val era: String = "",
    val verse: String,
    val meaning: String,
    val bhavartha: String,
    val difficultWords: List<DifficultWord>
)