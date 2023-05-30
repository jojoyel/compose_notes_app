package com.jojo.compose_notes_app.notes.data.mapper

import com.jojo.compose_notes_app.notes.data.local.entity.NoteEntity
import com.jojo.compose_notes_app.notes.domain.model.Note

fun NoteEntity.toNote(): Note {
    return Note(
        this.id,
        this.title,
        this.content,
        this.color,
        this.favorite
    )
}

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = this.id,
        title = this.title,
        content = this.content,
        color = this.color,
        favorite = this.favorite
    )
}