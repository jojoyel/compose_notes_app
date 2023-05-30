package com.jojo.compose_notes_app.util.notes.data.mapper

import com.jojo.compose_notes_app.util.notes.data.local.entity.NoteEntity
import com.jojo.compose_notes_app.util.notes.domain.model.Note

fun NoteEntity.toNote(): Note {
    return Note(
        this.id,
        this.title,
        this.content,
        this.color,
        this.favorite
    )
}