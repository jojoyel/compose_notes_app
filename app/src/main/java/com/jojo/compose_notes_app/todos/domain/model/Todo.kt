package com.jojo.compose_notes_app.todos.domain.model

data class Todo(
    val id: Int? = null,
    val title: String,
    val urgent: Boolean,
    val completed: Boolean = false,
    val creationDate: String? = null,
    val completedDate: String? = null
)
