package net.softglobe.quizincompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_attempts")
data class QuizAttempt(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val score: Int,
    val totalScore: Int,
    val passed: Boolean,
    val completedAt: Long
)
