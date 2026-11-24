package net.softglobe.quizincompose

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.softglobe.quizincompose.model.QuizAttempt
import net.softglobe.quizincompose.model.QuizDatabase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class QuizHistoryDatabaseTest {
    @Test
    fun migrationPreservesQuestionsAndAttemptsPersistAcrossReopen() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val databaseName = "quiz-history-${UUID.randomUUID()}.db"
        context.deleteDatabase(databaseName)

        val versionOneDatabase = context.openOrCreateDatabase(
            databaseName,
            Context.MODE_PRIVATE,
            null
        )
        versionOneDatabase.execSQL(
            """
            CREATE TABLE `questions` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT,
                `question` TEXT NOT NULL,
                `options` TEXT NOT NULL,
                `correctAnswerIndex` INTEGER NOT NULL
            )
            """.trimIndent()
        )
        versionOneDatabase.execSQL(
            "INSERT INTO `questions` (`question`, `options`, `correctAnswerIndex`) " +
                "VALUES ('Saved question', '[\"A\",\"B\"]', 1)"
        )
        versionOneDatabase.execSQL(
            "CREATE TABLE `room_master_table` (`id` INTEGER PRIMARY KEY, `identity_hash` TEXT)"
        )
        versionOneDatabase.execSQL(
            "INSERT OR REPLACE INTO `room_master_table` (`id`, `identity_hash`) VALUES (42, 'v1')"
        )
        versionOneDatabase.version = 1
        versionOneDatabase.close()

        val migratedDatabase = Room.databaseBuilder(
            context,
            QuizDatabase::class.java,
            databaseName
        ).addMigrations(QuizDatabase.MIGRATION_1_2).build()

        val attempt = QuizAttempt(
            score = 2,
            totalQuestions = 3,
            completedAt = 1_800_000_000_000
        )
        val olderAttempt = QuizAttempt(
            score = 1,
            totalQuestions = 3,
            completedAt = 1_700_000_000_000
        )
        try {
            val questions = migratedDatabase.quizDao().getAllQuestions().first()
            assertEquals(1, questions.size)
            assertEquals("Saved question", questions.first().question)

            migratedDatabase.quizDao().insertQuizAttempt(olderAttempt)
            migratedDatabase.quizDao().insertQuizAttempt(attempt)
        } finally {
            migratedDatabase.close()
        }

        val reopenedDatabase = Room.databaseBuilder(
            context,
            QuizDatabase::class.java,
            databaseName
        ).addMigrations(QuizDatabase.MIGRATION_1_2).build()
        try {
            val attempts = reopenedDatabase.quizDao().getQuizAttempts().first()
            assertEquals(2, attempts.size)
            assertEquals(attempt.score, attempts.first().score)
            assertEquals(attempt.totalQuestions, attempts.first().totalQuestions)
            assertEquals(attempt.completedAt, attempts.first().completedAt)
            assertEquals(olderAttempt.completedAt, attempts.last().completedAt)
        } finally {
            reopenedDatabase.close()
            context.deleteDatabase(databaseName)
        }
    }
}
