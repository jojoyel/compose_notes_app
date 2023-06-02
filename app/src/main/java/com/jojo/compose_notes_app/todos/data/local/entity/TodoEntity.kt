package com.jojo.compose_notes_app.todos.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val urgent: Boolean,
    @ColumnInfo(defaultValue = "false") val completed: Boolean,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP") val creationDate: String,
    val completedDate: String
)