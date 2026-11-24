package net.softglobe.quizincompose.model

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    @Upsert
    suspend fun insertQuestions(questions : List<QuestionModel>)

    @Query("SELECT * FROM questions")
    fun getAllQuestions(): Flow<List<QuestionModel>>

    @Upsert
    suspend fun insertQuizAttempt(attempt: QuizAttempt)

    @Query("SELECT * FROM quiz_attempts ORDER BY completedAt DESC, id DESC")
    fun getQuizAttempts(): Flow<List<QuizAttempt>>
}