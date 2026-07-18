package net.softglobe.quizincompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "questions")
data class QuestionModel(
    @PrimaryKey(autoGenerate = true)
    val id : Int? = null,
    val question : String,
    val options : List<String>,
    val correctAnswerIndex : Int
)