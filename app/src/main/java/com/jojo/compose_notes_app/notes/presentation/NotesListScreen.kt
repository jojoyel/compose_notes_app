package com.jojo.compose_notes_app.notes.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jojo.compose_notes_app.R
import com.jojo.compose_notes_app.notes.domain.model.Note

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesListScreen(state: NotesListState, noteClicked: (id: Int) -> Unit) {
    if (state.notes.isNotEmpty())
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        Modifier
                            .weight(1f)
                            .padding(start = 28.dp, end = 8.dp, top = 28.dp, bottom = 28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            stringResource(id = R.string.my_notes),
                            fontSize = MaterialTheme.typography.displaySmall.fontSize,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${state.notes.size}",
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )
                    }
                    IconButton(
                        onClick = { /*TODO*/ },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Default.Sort, contentDescription = null) // TODO: contentDesc
                    }
                }
            }
            items(state.notes, key = { item -> item.id!! }) {
                NoteItem(
                    modifier = Modifier.animateItemPlacement(),
                    note = it
                ) { noteClicked(it.id!!) }
            }
            item {
                Divider(Modifier.padding(vertical = 12.dp))
            }
        }
    else
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TODO: IMAGE HERE
            Text(stringResource(id = R.string.tip_create_note))
        }
}

@Composable
private fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        color = note.color?.let { Color(it) } ?: run { MaterialTheme.colorScheme.primaryContainer },
        contentColor = note.color?.let { contentColorFor(Color(it)) }
            ?: run { MaterialTheme.colorScheme.onPrimaryContainer },
        onClick = onClick,
        shape = RoundedCornerShape(6.dp)
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            if (note.title.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        note.title,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (note.favorite)
                        Icon(
                            Icons.Default.Star,
                            modifier = Modifier.padding(8.dp),
                            contentDescription = stringResource(id = R.string.content_desc_favorite)
                        )
                }
            }
            Text(text = note.content, maxLines = if (note.title.isNotBlank()) 2 else 4)
        }
    }
}