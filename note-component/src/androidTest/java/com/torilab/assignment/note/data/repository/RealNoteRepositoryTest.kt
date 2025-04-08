package com.torilab.assignment.note.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.torilab.assignment.note.data.db.NoteDAO
import com.torilab.assignment.note.data.db.NoteDatabase
import com.torilab.assignment.note.domain.model.Note
import java.util.Date
import kotlin.random.Random
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealNoteRepositoryTest {

    private lateinit var database: NoteDatabase
    private lateinit var noteDAO: NoteDAO
    private lateinit var sut: RealNoteRepository

    @Before
    fun setUp() {
        database =
            Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), NoteDatabase::class.java).build()
        noteDAO = database.dao()
        sut = RealNoteRepository(noteDAO)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun canGetSavedNote() = runTest {
        val insertResult = sut.insertNote(Note(id = 1, title = "title", note = "note"))
        val getResult = sut.getNoteById(1)

        assertTrue(insertResult.isSuccess)
        assertEquals(insertResult.getOrNull(), 1L)
        assertTrue(getResult.isSuccess)
        assertEquals(getResult.getOrNull()?.title, "title")
        assertEquals(getResult.getOrNull()?.note, "note")
    }

    @Test
    fun canGetSavedNotesSortedByCreatedDateDesc() = runTest {
        val notes = List(100) { Note(id = it + 1, title = "title$it", note = "note$it", createdAt = randomDate()) }
        notes.forEach { sut.insertNote(it) }
        val getResult = sut.getNoteList(pageSize = 10, offset = 20)
        val createdDates = getResult.getOrNull()?.map { it.createdAt }

        assertTrue(getResult.isSuccess)
        assertEquals(getResult.getOrNull()?.size, 10)
        assertEquals(createdDates!!.sortedDescending(), createdDates)
    }

    @Test
    fun canObserveStatusOfSavedNote() = runTest {
        sut.insertNote(Note(id = 1, title = "title", note = "note"))
        val getFlowResult = sut.getNoteFlowById(1).getOrNull()

        assertNotNull(getFlowResult)
        val note = getFlowResult?.first()
        assertEquals(note?.title, "title")
        assertEquals(note?.note, "note")

        sut.updateNote(Note(id = 1, title = "newTitle", note = "newNote"))
        val updatedNote = getFlowResult?.first()
        assertEquals(updatedNote?.title, "newTitle")
        assertEquals(updatedNote?.note, "newNote")
    }

    @Test
    fun canDeleteASavedNote() = runTest {
        val notes = List(10) { Note(id = it + 1, title = "title$it", note = "note$it") }
        notes.forEach { sut.insertNote(it) }
        val getResult = sut.getNoteList(pageSize = 10, offset = 0)
        assertTrue(getResult.isSuccess)
        assertEquals(getResult.getOrNull()?.size, 10)

        val deleteResult = sut.deleteNote(8)
        val getDeletedNoteResult = sut.getNoteById(8)
        assertTrue(deleteResult.isSuccess)
        assertTrue(getDeletedNoteResult.isError)
    }
}

private fun randomDate(): Date {
    val currentTime = System.currentTimeMillis()
    val oneYearMillis = 365L * 24 * 60 * 60 * 1000
    val randomPastTime = currentTime - Random.nextLong(oneYearMillis)
    return Date(randomPastTime)
}
