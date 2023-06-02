package com.jojo.compose_notes_app.todos.presentation

import com.jojo.compose_notes_app.todos.domain.model.Todo

sealed class TodosListEvent {
    object OnCloseDialog : TodosListEvent()
    data class OnOpenDialog(val todo: Todo? = null) : TodosListEvent()
    data class OnCreateTask(val todo: Todo) : TodosListEvent()
}
