package com.composeplayground.feature.details

import com.composeplayground.ui.uievent.UiEvent

sealed interface DetailsUiEvent : UiEvent {
    data class OnRepoClick(val url: String) : DetailsUiEvent
}