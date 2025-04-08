package com.torilab.assignment.foundations.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun CoroutineScope.launchWithExceptionHandler(
    dispatcher: CoroutineDispatcher? = null,
    onException: (Throwable) -> Unit = {},
    exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        print(throwable.stackTrace.joinToString("\n"))
        onException(throwable)
    },
    function: suspend CoroutineScope.() -> Unit,
): Job {
    val combinedContext = if (dispatcher != null) dispatcher + exceptionHandler else exceptionHandler
    return launch(combinedContext) { function() }
}
