package com.torilab.assignment.note.data.repository

import com.torilab.assignment.foundations.Answer
import com.torilab.assignment.note.data.db.NoteDAO
import com.torilab.assignment.note.data.model.NoteDatabaseModel
import com.torilab.assignment.note.domain.model.AddNoteError
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.note.domain.model.UpdateNoteError
import com.torilab.assignment.note.domain.repository.NoteRepository
import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

internal class RealNoteRepository(private val dao: NoteDAO) : NoteRepository {

    override suspend fun getNoteList(pageSize: Int, offset: Int): Answer<List<Note>, Unit> {
        return try {
            val dbModels = dao.getNotes(pageSize, offset)
            val domainModels = dbModels.map { mapDatabaseModelToDomainModel(it) }
            Answer.Success(domainModels)
        } catch (e: Exception) {
            Answer.Error(Unit)
        }
    }

    override suspend fun insertNote(note: Note): Answer<Long, AddNoteError> {
        // Test function to insert 200 dummy records into the database so you can test pagination.
        // To use: Uncomment ==> Build ==> Add one Note ==> Comment again => Build => Reopen app.
        // return try {
        //     val now = Date()
        //     val dummyNotes = List(200) {
        //         NoteDatabaseModel(
        //             id = 0,
        //             title = "Title: Note number $it",
        //             note = "Content: Note number $it",
        //             createdAt = Date(now.time + 60_000 * it),
        //         )
        //     }.toTypedArray()
        //     dao.insertNotes(*dummyNotes)
        //     Answer.Success(1L)
        // } catch (e: Exception) {
        //     Answer.Error(AddNoteError.GeneralError)
        // }

        return try {
            val newRowId = dao.insertNote(mapDomainModelToDatabaseModel(note))
            Answer.Success(newRowId)
        } catch (e: Exception) {
            Answer.Error(AddNoteError.GeneralError)
        }
    }

    override suspend fun getNoteById(id: Int): Answer<Note, Unit> {
        return try {
            val dbModel = dao.getNoteById(id)
            val domainModel = mapDatabaseModelToDomainModel(dbModel)
            Answer.Success(domainModel)
        } catch (e: Exception) {
            Answer.Error(Unit)
        }
    }

    override fun getNoteFlowById(id: Int): Answer<Flow<Note>, Unit> {
        return try {
            val flow = dao.getNoteFlowById(id).filterNotNull().map {
                mapDatabaseModelToDomainModel(it)
            }
            Answer.Success(flow)
        } catch (e: Exception) {
            Answer.Error(Unit)
        }
    }

    override suspend fun updateNote(note: Note): Answer<Unit, UpdateNoteError> {
        return try {
            val affectedRows =
                dao.updateNote(mapDomainModelToDatabaseModel(note).copy(updatedAt = Date(System.currentTimeMillis())))
            if (affectedRows > 0)
                Answer.Success(Unit)
            else
                Answer.Error(UpdateNoteError.GeneralError)
        } catch (e: Exception) {
            Answer.Error(UpdateNoteError.GeneralError)
        }
    }

    override suspend fun deleteNote(id: Int): Answer<Unit, Unit> {
        return try {
            val affectedRows = dao.deleteNote(id)
            if (affectedRows == 1)
                Answer.Success(Unit)
            else
                Answer.Error(Unit)
        } catch (e: Exception) {
            Answer.Error(Unit)
        }
    }

    private fun mapDatabaseModelToDomainModel(databaseModel: NoteDatabaseModel): Note {
        return Note(
            id = databaseModel.id,
            title = databaseModel.title,
            note = databaseModel.note,
            createdAt = databaseModel.createdAt,
            updatedAt = databaseModel.updatedAt,
        )
    }

    private fun mapDomainModelToDatabaseModel(domainModel: Note): NoteDatabaseModel {
        return NoteDatabaseModel(
            id = domainModel.id,
            title = domainModel.title,
            note = domainModel.note,
            createdAt = domainModel.createdAt,
            updatedAt = domainModel.updatedAt,
        )
    }
}
