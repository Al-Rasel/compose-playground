package com.composeplayground.ui.uievent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalUiEventDispatch = staticCompositionLocalOf<((UiEvent) -> Unit)?> { null }

@Composable
fun UiEventHandler(
    handle: (event: UiEvent) -> Unit,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalUiEventDispatch provides { handle(it) }) {
        content()
    }
}

@Composable
fun rememberUiEventDispatcher(): (event: UiEvent) -> Unit {
    val dispatch = LocalUiEventDispatch.current ?: error("UiEventHandler is not provided")
    return remember { { event -> dispatch(event) } }
}

interface UiEvent