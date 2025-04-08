package com.torilab.assignment.note.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "note_table")
internal data class NoteDatabaseModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "created_at") val createdAt: Date = Date(System.currentTimeMillis()),
    @ColumnInfo(name = "updated_at") val updatedAt: Date = Date(System.currentTimeMillis()),
)
