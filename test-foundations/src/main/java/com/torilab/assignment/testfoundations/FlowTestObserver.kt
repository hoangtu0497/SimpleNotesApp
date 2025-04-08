package com.torilab.assignment.testfoundations

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class FlowTestObserver<T : Any>(
    private val flow: Flow<T>,
    coroutineScope: CoroutineScope,
) {
    private val emittedValues = mutableListOf<T>()
    private val job: Job = flow.onEach {
        emittedValues.add(it)
    }.launchIn(coroutineScope)

    fun getValues() = emittedValues

    fun stopObserving() {
        job.cancel()
    }

    fun getFlow() = flow
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T : Any> Flow<T>.test(coroutineScope: CoroutineScope = CoroutineScope(UnconfinedTestDispatcher())) =
    FlowTestObserver(this, coroutineScope)
