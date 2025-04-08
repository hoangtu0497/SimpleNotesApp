package com.torilab.assignment.addnote.presentation.viewmodel

import com.torilab.assignment.foundations.Answer
import com.torilab.assignment.note.domain.model.AddNoteError
import com.torilab.assignment.note.domain.model.Note
import com.torilab.assignment.note.domain.usecase.AddNote
import com.torilab.assignment.testfoundations.FlowTestObserver
import com.torilab.assignment.testfoundations.RealScopeTestable
import com.torilab.assignment.testfoundations.ScopeTestable
import com.torilab.assignment.testfoundations.test
import com.torilab.assignment.viewmodel.EventDelegate
import com.torilab.assignment.viewmodel.StateDelegate
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RealAddNoteViewModelTest : ScopeTestable by RealScopeTestable() {

    @MockK
    private lateinit var addNote: AddNote
    private lateinit var stateObserver: FlowTestObserver<AddNoteScreenState>
    private lateinit var eventObserver: FlowTestObserver<AddNoteScreenEvent>
    private lateinit var sut: RealAddNoteViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        setupScope()
        sut = RealAddNoteViewModel(addNote, StateDelegate(AddNoteScreenState()), EventDelegate())
        stateObserver = sut.state.test()
        eventObserver = sut.viewEvent.test()
    }

    @After
    fun tearDown() {
        clearAllMocks()
        tearDownScope()
    }

    @Test
    fun `EXPECT default state WHEN initialized`() {
        assertEquals(listOf(AddNoteScreenState()), stateObserver.getValues())
    }

    @Test
    fun `EXPECT screen title updates WHEN typing in title`() {
        sut.onTitleChanged("Initial Title")
        sut.onTitleChanged("New Title")

        assertEquals(
            listOf(
                AddNoteScreenState(),
                AddNoteScreenState("Initial Title"),
                AddNoteScreenState("New Title"),
            ),
            stateObserver.getValues(),
        )
    }

    @Test
    fun `EXPECT screen description updates WHEN typing in description`() {
        sut.onDescriptionChanged("Initial Description")
        sut.onDescriptionChanged("New Description")

        assertEquals(
            listOf(
                AddNoteScreenState(),
                AddNoteScreenState(descriptionText = "Initial Description"),
                AddNoteScreenState(descriptionText = "New Description"),
            ),
            stateObserver.getValues(),
        )
    }

    @Test
    fun `EXPECT loading state then success event WHEN use case returns success`() = runTest {
        // GIVEN
        val noteSlot = slot<Note>()
        coEvery { addNote(capture(noteSlot)) } returns Answer.Success(1L)

        // WHEN
        sut.onTitleChanged("title")
        sut.onDescriptionChanged("description")
        sut.addNewNote()

        advanceTimeBy(5L)
        // THEN
        assertEquals(noteSlot.captured.title, "title")
        assertEquals(noteSlot.captured.note, "description")
        assertEquals(
            listOf(
                AddNoteScreenState(),
                AddNoteScreenState("title"),
                AddNoteScreenState("title", "description"),
                AddNoteScreenState("title", "description", AddNoteDisplayState.Loading),
                AddNoteScreenState("title", "description", AddNoteDisplayState.Input),
            ),
            stateObserver.getValues(),
        )
        assertEquals(listOf(AddNoteScreenEvent.SuccessfullyAdded(1)), eventObserver.getValues())
    }

    @Test
    fun `EXPECT no Exception WHEN use case returns Exception`() = runTest {
        // GIVEN
        coEvery { addNote(any()) } throws Exception()

        // WHEN
        sut.addNewNote()

        advanceTimeBy(5L)
        // THEN
        assertEquals(
            listOf(
                AddNoteScreenState(),
                AddNoteScreenState(displayState = AddNoteDisplayState.Loading),
                AddNoteScreenState(displayState = AddNoteDisplayState.Input),
            ),
            stateObserver.getValues(),
        )
    }

    @Test
    fun `EXPECT error event WHEN submit Note with empty title`() = runTest {
        // GIVEN
        val noteSlot = slot<Note>()
        coEvery { addNote(capture(noteSlot)) } returns Answer.Error(AddNoteError.EmptyTitle)

        // WHEN
        sut.addNewNote()

        advanceTimeBy(5L)
        // THEN
        assertEquals(
            listOf(
                AddNoteScreenState(),
                AddNoteScreenState(displayState = AddNoteDisplayState.Loading),
                AddNoteScreenState(displayState = AddNoteDisplayState.Input),
            ),
            stateObserver.getValues(),
        )
        assertEquals(noteSlot.captured.title, "")
        assertEquals(
            listOf(AddNoteScreenEvent.Error(com.torilab.assignment.designsystem.R.string.error_title_cannot_be_empty)),
            eventObserver.getValues(),
        )
    }

    @Test
    fun `EXPECT error event WHEN submit Note with empty description`() = runTest {
        // GIVEN
        val noteSlot = slot<Note>()
        coEvery { addNote(capture(noteSlot)) } returns Answer.Error(AddNoteError.EmptyDescription)

        // WHEN
        sut.onTitleChanged("title")
        sut.addNewNote()

        advanceTimeBy(5L)
        // THEN
        assertEquals(
            listOf(
                AddNoteScreenState(),
                AddNoteScreenState(titleText = "title"),
                AddNoteScreenState(titleText = "title", displayState = AddNoteDisplayState.Loading),
                AddNoteScreenState(titleText = "title", displayState = AddNoteDisplayState.Input),
            ),
            stateObserver.getValues(),
        )
        assertEquals(noteSlot.captured.title, "title")
        assertEquals(noteSlot.captured.note, "")
        assertEquals(
            listOf(AddNoteScreenEvent.Error(com.torilab.assignment.designsystem.R.string.error_description_cannot_be_empty)),
            eventObserver.getValues(),
        )
    }
}
