package io.github.codedrink5154.todoapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.codedrink5154.todoapp.data.TodoTask
import io.github.codedrink5154.todoapp.viewmodel.TodoViewModel

@Composable
fun TodoListScreen(
    innerPadding: PaddingValues,
    todoViewModel: TodoViewModel,
) {
    /* 任务列表 */
    val todos = todoViewModel.todoList
    /* 是否正在添加任务 */
    val isEditorOpen by todoViewModel.isEditorOpen.collectAsStateWithLifecycle()
    /* 是否正在编辑任务 */
    val isEditing by todoViewModel.isEditing.collectAsStateWithLifecycle()

    /* 编辑器的临时状态 */
    val draftTitle by todoViewModel.title.collectAsStateWithLifecycle()
    val draftDescript by todoViewModel.description.collectAsStateWithLifecycle()
    val draftDeadline by todoViewModel.deadline.collectAsStateWithLifecycle()
    val draftCategory by todoViewModel.category.collectAsStateWithLifecycle()
    val draftRemind by todoViewModel.remind.collectAsStateWithLifecycle()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "待办事项",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (todos.isEmpty()) {
                EmptyTodoState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 4.dp,
                        bottom = 96.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(items = todos, key = { it.id }) { task ->
                        TodoRow(
                            task = task,
                            onToggle = { todoViewModel.toggleDone(task.id) },
                            onClick = { todoViewModel.editTask(task.id) },
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = todoViewModel::addTask,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "添加任务",
            )
        }
    }

    if (isEditing) {
        EditTaskSheet(
            title = draftTitle,
            onTitleChange = todoViewModel::updateTitle,
            description = draftDescript,
            onDescriptionChange = todoViewModel::updateDescription,
            onDismiss = todoViewModel::closeEditor,
        )
    }

    if (isEditorOpen) {
        AddTodoSheet(
            title = draftTitle,
            onTitleChange = todoViewModel::updateTitle,
            onDismiss = todoViewModel::addTaskDone,
            onSend = todoViewModel::addNewTask,
        )
    }
}

@Composable
private fun EmptyTodoState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            // tint可以给图标着色,这里通过alpha来设置透明度
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "还没有任务",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "点击按钮添加任务",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun TodoRow(
    task: TodoTask,
    onToggle: () -> Unit,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = task.title,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp),
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (task.done) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                },
                color = if (task.done) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            IconToggleButton(
                checked = task.done,
                onCheckedChange = { onToggle() },
            ) {
                Icon(
                    imageVector = if (task.done) {
                        Icons.Outlined.RadioButtonChecked
                    } else {
                        Icons.Outlined.RadioButtonUnchecked
                    },
                    contentDescription = if (task.done) "标记为未完成" else "标记为完成",
                    tint = if (task.done) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTodoSheet(
    title: String,
    onTitleChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSend: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val focusRequester = remember { FocusRequester() }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(
                    WindowInsets.systemBars
                        .only(WindowInsetsSides.Bottom)
                        .union(WindowInsets.ime)
                )
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 8.dp,
                ),
        ) {
            TextField(
                value = title,
                onValueChange = onTitleChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = { Text("你想做什么？") },
                // 类似的，也有leadingIcon
                trailingIcon = {
                    IconButton(
                        onClick = onSend,
                        enabled = title.isNotBlank(),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "创建任务",
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                // 定制化颜色
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
            )
        }
    }

    /* 首次打开AddTodoSheet时自动聚焦 */
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskSheet(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    // category: String?,
    // onCategoryChange: (String) -> Unit,
    // deadline: Long?,
    // onDeadlineChange: (Long) -> Unit,
    // remind: Long?,
    // onremindChange: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    var sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val focusRequester = remember { FocusRequester() }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(
                    WindowInsets.systemBars
                        .only(WindowInsetsSides.Bottom)
                        .union(WindowInsets.ime)
                )
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 8.dp,
                ),
        ) {
            Text(
                text = "任务标题",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            TextField(
                value = title,
                onValueChange = { title -> onTitleChange(title) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                // 定制化颜色
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
            )
            Text(
                text = "任务描述",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            TextField(
                value = description,
                onValueChange = { desc -> onDescriptionChange(desc) },
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = false,
                shape = RoundedCornerShape(20.dp),
                // 定制化颜色
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
            )
        }

    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
