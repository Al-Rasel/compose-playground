package com.composeplayground.feature.search

import com.composeplayground.model.User
import com.composeplayground.ui.uievent.UiEvent

sealed interface SearchUiEvent : UiEvent {
    data class OnItemClick(val user: User) : SearchUiEvent
}