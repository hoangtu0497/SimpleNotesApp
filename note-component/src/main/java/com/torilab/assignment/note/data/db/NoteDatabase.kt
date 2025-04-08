package com.torilab.assignment.note.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.torilab.assignment.note.data.model.NoteDatabaseModel
import java.util.Date

private object DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

@Database(
    entities = [NoteDatabaseModel::class],
    version = 1,
)
@TypeConverters(DateConverters::class)
internal abstract class NoteDatabase : RoomDatabase() {
    abstract fun dao(): NoteDAO

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null
        private const val DATABASE_NAME = "notes_database"

        fun getInstance(context: Context): NoteDatabase {
            if (INSTANCE != null)
                return INSTANCE!!

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    DATABASE_NAME,
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
