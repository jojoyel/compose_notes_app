package com.jojo.compose_notes_app.todos.domain.di

import com.jojo.compose_notes_app.todos.domain.repository.TodosRepository
import com.jojo.compose_notes_app.todos.domain.use_case.DeleteTodo
import com.jojo.compose_notes_app.todos.domain.use_case.GetTodos
import com.jojo.compose_notes_app.todos.domain.use_case.InsertTodo
import com.jojo.compose_notes_app.todos.domain.use_case.TodosUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TodosDomainModule {

    @ViewModelScoped
    @Provides
    fun providesTodosUseCases(repository: TodosRepository): TodosUseCases {
        return TodosUseCases(
            getTodos = GetTodos(repository),
            insertTodo = InsertTodo(repository),
            deleteTodo = DeleteTodo(repository)
        )
    }
}