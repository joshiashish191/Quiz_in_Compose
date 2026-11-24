package net.softglobe.quizincompose.model

import android.app.Application
import androidx.room.Room

class QuizApp : Application() {
    val database by lazy {
        Room.databaseBuilder(
            this,
            QuizDatabase::class.java,
            "quizdb.db"
        ).addMigrations(QuizDatabase.MIGRATION_1_2).build()
    }

    val repository by lazy {
        Repository(database.quizDao())
    }
}