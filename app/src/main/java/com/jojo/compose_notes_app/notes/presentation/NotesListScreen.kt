package com.jojo.compose_notes_app.notes.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jojo.compose_notes_app.R
import com.jojo.compose_notes_app.notes.domain.model.Note
import com.jojo.compose_notes_app.util.SelectableColors

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class
)
@Composable
fun NotesListScreen(
    state: NotesListState,
    onEvent: (NotesListEvent) -> Unit,
    noteClicked: (id: Int) -> Unit
) {
    var filtersOpen by remember { mutableStateOf(false) }

    if (state.notes.isNotEmpty() || state.hasNotes)
        Column(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier.padding(16.dp)) {
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
                        AnimatedContent(targetState = state.notes.size, transitionSpec = {
                            if (targetState > initialState) {
                                slideInHorizontally { width -> width } + fadeIn() with
                                        slideOutHorizontally { width -> -width } + fadeOut()
                            } else {
                                slideInHorizontally { width -> -width } + fadeIn() with
                                        slideOutHorizontally { width -> width } + fadeOut()
                            }.using(SizeTransform(clip = false))
                        }) {
                            Text(
                                text = "$it",
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        }
                    }
                    IconButton(
                        onClick = { filtersOpen = !filtersOpen },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Default.Sort, contentDescription = null) // TODO: contentDesc
                    }
                }

                AnimatedVisibility(visible = filtersOpen) {
                    var selectedColor: SelectableColors? by remember { mutableStateOf(null) }

                    Column {
                        Text(text = "Colors", style = MaterialTheme.typography.bodyLarge)
                        FlowRow(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                onClick = {
                                    selectedColor = null
                                    onEvent(NotesListEvent.OnColorFilterChanged(null))
                                },
                                selected = selectedColor == null,
                                label = { Text(stringResource(id = R.string.all)) })
                            SelectableColors.values().forEach {
                                when (it) {
                                    SelectableColors.NONE -> {
                                        FilterChip(
                                            onClick = {
                                                selectedColor = it
                                                onEvent(NotesListEvent.OnColorFilterChanged(it))
                                            },
                                            selected = selectedColor == it,
                                            leadingIcon = {
                                                Icon(
                                                    painterResource(R.drawable.dot),
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primaryContainer
                                                )
                                            },
                                            label = { Text(stringResource(it.displayName)) })
                                    }

                                    else -> {
                                        FilterChip(
                                            onClick = {
                                                selectedColor = it
                                                onEvent(NotesListEvent.OnColorFilterChanged(it))
                                            },
                                            selected = selectedColor == it,
                                            leadingIcon = {
                                                Icon(
                                                    painterResource(R.drawable.dot),
                                                    contentDescription = null,
                                                    tint = Color(it.color)
                                                )
                                            },
                                            label = { Text(stringResource(it.displayName)) })
                                    }
                                }
                            }
                        }
                    }
                }
            }

            val lazyState = rememberLazyListState()

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight(),
                state = lazyState
            ) {

                items(state.notes, key = { it.title }) {
                    NoteItem(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(IntrinsicSize.Max)
                            .animateItemPlacement(),
                        note = it
                    ) { noteClicked(it.id!!) }
                }

                item {
                    Divider(Modifier.padding(vertical = 12.dp))
                }
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