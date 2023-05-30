package com.jojo.compose_notes_app.notes.domain.model

data class Note(
    val id: Int?,
    val title: String,
    val content: String,
    val color: Long?,
    val favorite: Boolean
)
