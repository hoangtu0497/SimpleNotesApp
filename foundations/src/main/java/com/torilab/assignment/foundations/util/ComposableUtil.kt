package com.torilab.assignment.foundations.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberDebouncedClick(
    debounceTimeMillis: Long = DEFAULT_DEBOUNCE_INTERVAL_MILLIS,
    onClick: () -> Unit,
): () -> Unit {
    var lastClickTimeMillis: Long by remember { mutableLongStateOf(value = 0L) }
    return {
        val now = System.currentTimeMillis()
        if (now - lastClickTimeMillis >= debounceTimeMillis) {
            lastClickTimeMillis = now
            onClick()
        }
    }
}

private const val DEFAULT_DEBOUNCE_INTERVAL_MILLIS = 1_000L
