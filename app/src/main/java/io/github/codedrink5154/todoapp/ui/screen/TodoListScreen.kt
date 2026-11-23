package io.github.codedrink5154.todoapp.ui.screen

import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sun.org.apache.xml.internal.utils.StringComparable
import io.github.codedrink5154.todoapp.data.TodoTask
import io.github.codedrink5154.todoapp.utils.instantToDateTime
import io.github.codedrink5154.todoapp.utils.longToInstant
import io.github.codedrink5154.todoapp.utils.toStringZHCN
import io.github.codedrink5154.todoapp.viewmodel.Page
import io.github.codedrink5154.todoapp.viewmodel.TodoViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop

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

    /* 添加任务时只需要标题 */
    val draftTitle by todoViewModel.title.collectAsStateWithLifecycle()


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
        EditTaskSheet(todoViewModel)
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
                // leadingIcon = {}
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

/* 底部面板 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskSheet(
    viewModel: TodoViewModel,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val page by viewModel.page.collectAsStateWithLifecycle()
    val deadline by viewModel.deadline.collectAsStateWithLifecycle()

    ModalBottomSheet(
        onDismissRequest = viewModel::closeEditor,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
    ) {
        when (page) {
            Page.OVERVIEW -> ShowTaskOverview(viewModel)
            Page.DATE -> ShowDatePicker(
                onValueChange = viewModel::updateDeadline,
            )

            Page.TIME -> ShowTimePicker(viewModel::updateDeadline)
        }
    }
}

@Composable
fun EditTextRow(
    label: String,
    value: String,
    singleLine: Boolean,
    onChange: (String) -> Unit,
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        TextField(
            value = value,
            onValueChange = { value -> onChange(value) },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = singleLine,
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

@Composable
fun EditTimeRow(label: String, value: Any?, onEdit: () -> Unit) {
    /* 显示的时间 */
    val time: String = when (value) {
        null -> "无时间"
        is Int -> value.toString()
        is Long -> (instantToDateTime(longToInstant(value))).toStringZHCN()
        else -> "似乎出现错误啦~"
    }

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilledIconButton(
                    onClick = onEdit,
                    modifier = Modifier.width(40.dp),
                ) {
                    Icon(
                        Icons.Default.Edit,
                        ""
                    )
                }
            }
        }
    }
}

@Composable
fun ShowTaskOverview(viewModel: TodoViewModel) {
    val title by viewModel.title.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val category by viewModel.category.collectAsStateWithLifecycle()
    val deadline by viewModel.deadline.collectAsStateWithLifecycle()
    val remind by viewModel.remind.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
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
                bottom = 12.dp,
            )
    ) {
        EditTextRow("标题", title, true, viewModel::updateTitle)
        EditTextRow("描述", description, false, viewModel::updateDescription)
        EditTextRow("分类", category ?: "", true, viewModel::updateCategory)
        EditTimeRow("截止时间", deadline) { viewModel.switchPage(Page.DATE) }
        EditTimeRow("提醒时间", remind) { /* TODO: 打开提醒时间编辑器 */ }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDatePicker(onValueChange: (Long?) -> Unit, viewModel: TodoViewModel = viewModel()) {
    var dateTime = viewModel.deadline.collectAsStateWithLifecycle().value
    var instant = if (dateTime != null) Instant.ofEpochMilli(dateTime) else Instant.now()
    var datePickerState = rememberDatePickerState(
        initialSelectedDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    )
    val timeStr: String = instant.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"))

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
                bottom = 12.dp,
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
        ) {
            Button(
                onClick = {
                    onValueChange(datePickerState.selectedDateMillis)
                }
            ) {
                Text("确定")
            }
        }
        DatePicker(
            state = datePickerState
        )
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.padding(4.dp).fillMaxWidth().clickable { viewModel.switchPage(Page.TIME) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row {
                Icon(Icons.Default.Alarm, "代表时间的图标")
                Spacer(Modifier.width(4.dp))
                Text(timeStr)
            }
            Icon(Icons.Default.Edit, "编辑具体时间")
        }
    }
}

@Composable
fun ShowTimePicker(onValueChange: (Long?) -> Unit, viewModifier: TodoViewModel = viewModel()) {
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
                bottom = 12.dp,
            )
    ) {
        Text("测试")
    }
}
