package net.softglobe.quizincompose.model

import kotlinx.coroutines.flow.Flow

class Repository(private val quizDao: QuizDao) {

    suspend fun insertQuestions(questions : List<QuestionModel>) {
        return quizDao.insertQuestions(questions)
    }

    fun getAllQuestions() : Flow<List<QuestionModel>> {
        return quizDao.getAllQuestions()
    }

    suspend fun insertQuizAttempt(attempt: QuizAttempt) {
        quizDao.insertQuizAttempt(attempt)
    }

    fun getQuizAttempts(): Flow<List<QuizAttempt>> {
        return quizDao.getQuizAttempts()
    }
}