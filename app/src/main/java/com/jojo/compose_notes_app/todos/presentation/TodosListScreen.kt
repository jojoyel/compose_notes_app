package com.jojo.compose_notes_app.todos.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.jojo.compose_notes_app.R
import com.jojo.compose_notes_app.todos.domain.model.Todo
import com.jojo.compose_notes_app.ui.theme.NotesAppTheme
import com.jojo.compose_notes_app.ui.urgentColor

@Composable
fun TodosListScreen(state: TodosListState, event: (TodosListEvent) -> Unit) {
    if (state.todos.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.todos, key = { item -> item.id!! }) {
                TodoItem(todo = it) {
                    event(TodosListEvent.OnOpenDialog(it))
                }
            }
        }
    } else
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TODO: IMAGE HERE
            Text(stringResource(id = R.string.tip_create_todo))
        }

    if (state.editionDialogVisible)
        TodoDialog(
            data = state.editionDialogData,
            onEdit = { event(TodosListEvent.OnCreateTask(it)) }) {
            event(TodosListEvent.OnCloseDialog)
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDialog(data: Todo? = null, onEdit: (Todo) -> Unit, onClose: () -> Unit) {
    AlertDialog(onDismissRequest = onClose) {
        Surface(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            DialogContent(todo = data, onClose = onClose, onEdit = onEdit)
        }
    }
}

@Composable
private fun DialogContent(
    todo: Todo?,
    onClose: () -> Unit,
    onEdit: (Todo) -> Unit
) {
    var title by remember { mutableStateOf(todo?.title ?: "") }
    var urgent by remember { mutableStateOf(todo?.urgent ?: false) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(if (todo == null) R.string.create_task else R.string.task),
            fontSize = MaterialTheme.typography.headlineSmall.fontSize
        )
        Divider(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.hint_task_name)) },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            Modifier
                .clip(RoundedCornerShape(4.dp))
                .toggleable(
                    value = urgent,
                    onValueChange = { urgent = it },
                    role = Role.Checkbox
                )
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = urgent,
                onCheckedChange = null,
                modifier = Modifier.padding(2.dp)
            )
            Text(stringResource(R.string.urgent), modifier = Modifier.weight(1f))
        }
        if (todo != null) {
            if (todo.completed)
                Button(onClick = {
                    onEdit(
                        Todo(
                            id = todo.id,
                            title = title,
                            urgent = urgent,
                            completed = false
                        )
                    )
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.not_completed))
                }
            else
                Button(onClick = {
                    onEdit(
                        Todo(
                            id = todo.id,
                            title = title,
                            urgent = urgent,
                            completed = true
                        )
                    )
                }, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        Icons.Default.TaskAlt,
                        contentDescription = null
                    ) // TODO: contentDesc
                    Text(stringResource(R.string.completed))
                }
        }

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onClose) {
                Text(stringResource(R.string.action_cancel))
            }

            if (todo != null)
                TextButton(onClick = {
                    onEdit(
                        Todo(
                            id = todo.id,
                            title = title,
                            urgent = urgent
                        )
                    )
                }) {
                    Text(stringResource(R.string.action_modify))
                }
            else
                TextButton(
                    enabled = title.isNotBlank(),
                    onClick = { onEdit(Todo(title = title, urgent = urgent)) }) {
                    Text(stringResource(R.string.action_create))
                }
        }
    }
}

@Composable
private fun TodoItem(
    modifier: Modifier = Modifier,
    todo: Todo,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (todo.urgent && !todo.completed) urgentColor(isSystemInDarkTheme()).copy(
                alpha = .5f
            )
            else if (!todo.completed) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .8f)
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (todo.urgent && !todo.completed)
                Icon(
                    Icons.Default.NotificationsActive,
                    contentDescription = null, // TODO: contentDesc

                )
            Text(
                todo.title,
                textDecoration = if (todo.completed) TextDecoration.LineThrough else null
            )
        }
    }
}

// Previews

@Preview(
    showSystemUi = true,
    showBackground = true,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
    name = "Dialog preview"
)
@Composable
fun DialogPreview() {
    NotesAppTheme {
        TodoDialog(onEdit = {}, data = Todo(1, "Clean my desktop", false), onClose = {})
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
    name = "Todo items preview"
)
@Composable
fun TodoItemsPreview() {
    NotesAppTheme {
        TodosListScreen(
            state = TodosListState(
                todos = listOf(
                    Todo(1, "Clean my desktop", false),
                    Todo(2, "Answer to important mails", true),
                    Todo(3, "Finish my app", true, completed = true)
                )
            ), event = {}
        )
    }
}