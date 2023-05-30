package com.jojo.compose_notes_app.notes.presentation.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jojo.compose_notes_app.ui.composables.NoBordersTextField
import com.jojo.compose_notes_app.util.SelectableColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun NoteDetailsScreen(state: NoteDetailsState, onEvent: (NoteDetailsEvent) -> Unit) {
    if (state.loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else
        Column(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NoBordersTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.note.title,
                    onValueChange = { onEvent(NoteDetailsEvent.OnTitleChanged(it)) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleLarge,
                    placeholder = {
                        Text(
                            "Titre", // TODO: res
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                    }
                )
                Divider(Modifier.fillMaxWidth())
                NoBordersTextField(
                    value = state.note.content,
                    onValueChange = { onEvent(NoteDetailsEvent.OnContentChanged(it)) },
                    placeholder = { Text("Note") }, // TODO: res
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
            NoteDetailsBottomAppBar(state = state, onEvent = onEvent)
        }
}

@Composable
fun NoteDetailsBottomAppBar(
    state: NoteDetailsState,
    onEvent: (NoteDetailsEvent) -> Unit
) {

    var panel by remember { mutableStateOf(NoteDetailsBottomAppBarPanel.MAIN) }

    BottomAppBar(actions = {
        BottomAppBarContent(
            panel = panel,
            onPanelChange = { panel = it },
            state = state,
            onEvent = onEvent
        )
    }, floatingActionButton = {
        AnimatedVisibility(
            visible = panel == NoteDetailsBottomAppBarPanel.MAIN,
            enter = slideInHorizontally { it / 2 } + fadeIn(),
            exit = slideOutHorizontally { it / 2 } + fadeOut()
        ) {
            if (state.originalNote == null) FloatingActionButton(onClick = {
                onEvent(NoteDetailsEvent.OnCreateNoteClicked)
            }) {
                Icon(Icons.Default.Done, contentDescription = null) // TODO: content desc
            }
            else FloatingActionButton(onClick = { }) {
                Icon(Icons.Default.Share, contentDescription = null) // TODO: content desc
            }
        }
    })
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomAppBarContent(
    panel: NoteDetailsBottomAppBarPanel,
    onPanelChange: (NoteDetailsBottomAppBarPanel) -> Unit,
    state: NoteDetailsState,
    onEvent: (NoteDetailsEvent) -> Unit
) {

    AnimatedContent(targetState = panel, transitionSpec = {
        slideInVertically { it / 2 } + fadeIn() with slideOutVertically { -it / 2 } + fadeOut()
    }) {
        when (it) {
            NoteDetailsBottomAppBarPanel.MAIN -> {
                Row {
                    if (state.originalNote != null)
                        IconButton(onClick = { onPanelChange(NoteDetailsBottomAppBarPanel.DELETE) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null // TODO: contentDesc
                            )
                        }
                    IconButton(onClick = { onEvent(NoteDetailsEvent.OnFavoriteToggled) }) {
                        Icon(
                            imageVector = if (state.note.favorite) Icons.Default.StarRate else Icons.Outlined.StarRate,
                            contentDescription = if (state.note.favorite) null else null // TODO: contentDesc
                        )
                    }
                    IconButton(onClick = { onPanelChange(NoteDetailsBottomAppBarPanel.COLOR) }) {
                        val surface = MaterialTheme.colorScheme.surface

                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val radius = size.height / 3
                            if (state.note.color != null)
                                drawCircle(color = Color(state.note.color), radius = radius)
                            else {
                                drawCircle(color = surface, radius = radius)
                                drawLine(
                                    Color.Red,
                                    start = Offset(
                                        (size.width / 2 + cos(45f.toRadians()) * radius).toFloat(),
                                        (size.height / 2 - sin(45f.toRadians()) * radius).toFloat()
                                    ),
                                    end = Offset(
                                        (size.width / 2 + cos(225f.toRadians()) * radius).toFloat(),
                                        (size.height / 2 - sin(225f.toRadians()) * radius).toFloat()
                                    ),
                                    strokeWidth = 5f
                                )
                            }
                        }
                    }
                }
            }

            NoteDetailsBottomAppBarPanel.COLOR -> {
                Row {
                    IconButton(onClick = { onPanelChange(NoteDetailsBottomAppBarPanel.MAIN) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null // TODO: contentDesc
                        )
                    }
                    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        SelectableColors.values().forEach { color ->
                            ColorButton(color = color) {
                                onPanelChange(NoteDetailsBottomAppBarPanel.MAIN)
                                onEvent(NoteDetailsEvent.OnColorChanged(if (color == SelectableColors.NONE) null else color.color))
                            }
                        }
                    }
                }
            }

            NoteDetailsBottomAppBarPanel.DELETE -> {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Supprimer ?",
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    ) // TODO: res
                    Spacer(modifier = Modifier.weight(1f))
                    AssistChip(
                        onClick = { onPanelChange(NoteDetailsBottomAppBarPanel.MAIN) },
                        label = { Text("Non, conserver") })
                    Spacer(Modifier.width(4.dp))
                    AssistChip(
                        onClick = { onEvent(NoteDetailsEvent.OnDeleteClicked) },
                        label = { Text("Supprimer") })
                }
            }
        }
    }
}

enum class NoteDetailsBottomAppBarPanel {
    MAIN, COLOR, DELETE
}

@Composable
fun ColorButton(modifier: Modifier = Modifier, color: SelectableColors, onClick: () -> Unit) {
    IconButton(modifier = Modifier.then(modifier), onClick = onClick) {
        val surface = MaterialTheme.colorScheme.surface

        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.height / 3

            when (color) {
                SelectableColors.NONE -> {
                    drawCircle(color = surface, radius = radius)
                    drawLine(
                        Color.Red,
                        start = Offset(
                            (size.width / 2 + cos(45f.toRadians()) * radius).toFloat(),
                            (size.height / 2 - sin(45f.toRadians()) * radius).toFloat()
                        ),
                        end = Offset(
                            (size.width / 2 + cos(225f.toRadians()) * radius).toFloat(),
                            (size.height / 2 - sin(225f.toRadians()) * radius).toFloat()
                        ),
                        strokeWidth = 5f
                    )
                }

                else -> drawCircle(color = Color(color.color), radius = size.height / 3)
            }
        }
    }
}

private fun Float.toRadians(): Double = (this * PI / 180)