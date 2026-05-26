package net.softglobe.quizincompose

data class QuestionModel(
    val question : String,
    val options : List<String>,
    val correctAnswerIndex : Int
)