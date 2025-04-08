package com.torilab.assignment.note.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.torilab.assignment.note.data.model.NoteDatabaseModel
import kotlinx.coroutines.flow.Flow

@Dao
internal interface NoteDAO {

    @Query("SELECT * FROM note_table ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    suspend fun getNotes(limit: Int, offset: Int): List<NoteDatabaseModel>

    @Query("SELECT * FROM note_table WHERE id = :id")
    suspend fun getNoteById(id: Int): NoteDatabaseModel

    @Query("SELECT * FROM note_table WHERE id = :id")
    fun getNoteFlowById(id: Int): Flow<NoteDatabaseModel?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteDatabaseModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(vararg note: NoteDatabaseModel)

    @Update
    suspend fun updateNote(note: NoteDatabaseModel): Int

    @Query("DELETE FROM note_table WHERE id = :id")
    suspend fun deleteNote(id: Int): Int
}
