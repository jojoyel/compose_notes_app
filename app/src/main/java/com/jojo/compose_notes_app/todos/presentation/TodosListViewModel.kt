package com.jojo.compose_notes_app.todos.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jojo.compose_notes_app.todos.domain.use_case.TodosUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosListViewModel @Inject constructor(private val useCases: TodosUseCases) : ViewModel() {

    var state by mutableStateOf(TodosListState())
        private set

    init {
        viewModelScope.launch {
            useCases.getTodos().collect { notes ->
                state = state.copy(todos = notes)
            }
        }
    }

    fun onEvent(event: TodosListEvent) {
        when (event) {
            TodosListEvent.OnCloseDialog -> state = state.copy(editionDialogVisible = false)

            is TodosListEvent.OnCreateTask -> {
                viewModelScope.launch {
                    useCases.insertTodo(event.todo)
                    state = state.copy(editionDialogVisible = false, editionDialogData = null)
                }
            }

            is TodosListEvent.OnOpenDialog -> state =
                state.copy(editionDialogVisible = true, editionDialogData = event.todo)

            is TodosListEvent.OnDeleteTask -> {
                viewModelScope.launch {
                    useCases.deleteTodo(event.todo)
                    state = state.copy(editionDialogVisible = false, editionDialogData = null)
                }
            }
        }
    }
}