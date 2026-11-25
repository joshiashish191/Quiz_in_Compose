package net.softglobe.quizincompose.model

import kotlinx.coroutines.flow.Flow

class Repository(private val quizDao: QuizDao) {

    suspend fun insertQuestions(questions : List<QuestionModel>) {
        return quizDao.insertQuestions(questions)
    }

    fun getAllQuestions() : Flow<List<QuestionModel>> {
        return quizDao.getAllQuestions()
    }

    suspend fun insertAttempt(attempt: QuizAttempt) {
        quizDao.insertAttempt(attempt)
    }

    fun getAllAttempts(): Flow<List<QuizAttempt>> {
        return quizDao.getAllAttempts()
    }
}