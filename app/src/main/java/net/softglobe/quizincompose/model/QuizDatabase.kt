package net.softglobe.quizincompose.model

import androidx.room.migration.Migration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [QuestionModel::class, QuizAttempt::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDao() : QuizDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `quiz_attempts` " +
                        "( `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " `score` INTEGER NOT NULL," +
                        " `totalScore` INTEGER NOT NULL," +
                        " `passed` INTEGER NOT NULL," +
                        " `completedAt` INTEGER NOT NULL)"
                )
            }
        }
    }
}