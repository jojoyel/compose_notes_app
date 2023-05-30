package com.jojo.compose_notes_app.util

sealed class UiEvent {
    object GoBack : UiEvent()
    data class GoBackWithMessage(val message: UiText) : UiEvent()
    data class ShowSnackbar(val message: UiText) : UiEvent()
}
