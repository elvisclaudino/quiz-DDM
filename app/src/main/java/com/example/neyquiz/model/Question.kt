package com.example.neyquiz.model

data class Question(
    val text: String,
    val imageUrl: String,
    val options: List<String>,
    val correctAnswerIndex: Int
) {
    fun shuffledOptions(): List<String> {
        return options.shuffled()
    }
}