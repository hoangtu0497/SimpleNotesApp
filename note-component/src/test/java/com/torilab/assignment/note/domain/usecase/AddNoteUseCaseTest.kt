package com.torilab.assignment.note.domain.usecase

import com.torilab.assignment.foundations.Answer
import com.torilab.assignment.note.domain.model.AddNoteError
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.note.domain.repository.NoteRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddNoteUseCaseTest {

    @MockK
    private lateinit var noteRepository: NoteRepository
    private lateinit var sut: AddNoteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        sut = AddNoteUseCase(noteRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `EXPECT result Success WHEN add note with full information`() = runTest {
        val note = Note(1, "title", "content")
        coEvery { noteRepository.insertNote(note) } returns Answer.Success(1L)

        val result = sut.invoke(note)

        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull(), 1L)
    }

    @Test
    fun `EXPECT result Error WHEN add note with empty title`() = runTest {
        val note = Note(1, "", "")
        coEvery { noteRepository.insertNote(note) } returns Answer.Success(1L)

        val result = sut.invoke(note)

        assertTrue(result.isError)
        assertEquals(result.getError(), AddNoteError.EmptyTitle)
    }

    @Test
    fun `EXPECT result Error WHEN add note with empty description`() = runTest {
        val note = Note(1, "title", "")
        coEvery { noteRepository.insertNote(note) } returns Answer.Success(1L)

        val result = sut.invoke(note)

        assertTrue(result.isError)
        assertEquals(result.getError(), AddNoteError.EmptyDescription)
    }

    @Test
    fun `EXPECT result Error WHEN cannot insert note into database`() = runTest {
        val note = Note(1, "title", "note")
        coEvery { noteRepository.insertNote(note) } returns Answer.Error(AddNoteError.GeneralError)

        val result = sut.invoke(note)

        assertTrue(result.isError)
        assertEquals(result.getError(), AddNoteError.GeneralError)
    }
}
