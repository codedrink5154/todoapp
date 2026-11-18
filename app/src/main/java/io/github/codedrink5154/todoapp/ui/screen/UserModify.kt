package io.github.codedrink5154.todoapp.ui.screen

import android.content.ClipData
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.codedrink5154.todoapp.data.AppState
import io.github.codedrink5154.todoapp.navigation.BackStack
import io.github.codedrink5154.todoapp.viewmodel.AuthViewModel
import sun.awt.X11.Visual

enum class EditableField {
    NAME, PASSWORD
}

@Composable
fun UserModify(innerPadding: PaddingValues) {

    // 表示编辑器是否开启
    var editing by rememberSaveable { mutableStateOf<EditableField?>(null) }

    when (val field = editing) {
        null -> Unit
        EditableField.NAME -> UserInfoEditDialog(
            title = "修改用户名",
            initialValue = AppState.user.name,
            onDismiss = { editing = null },
            onConfirm = { newName ->
                AppState.user = AppState.user.copy(name = newName.trim())
                editing = null
            },
        )

        EditableField.PASSWORD -> UserInfoEditDialog(
            title = "修改密码",
            initialValue = "",
            maskInput = true,
            onDismiss = { editing = null },
            onConfirm = { newPassword ->
                AppState.user = AppState.user.copy(password = newPassword)
                editing = null
            },
        )
    }
    // 展示可编辑的条目
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            EditRow(title = "用户名", content = AppState.user.name, onClick = { editing = EditableField.NAME })
        }
        item {
            EditRow(title = "密码", content = "********", onClick = { editing = EditableField.PASSWORD })
        }
    }
}

// 编辑所用对话框
@Composable
fun UserInfoEditDialog(
    title: String,
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    maskInput: Boolean = false,
) {
    var text by rememberSaveable(initialValue) { mutableStateOf(initialValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                visualTransformation = if (maskInput) {
                    PasswordVisualTransformation('*')
                } else {
                    VisualTransformation.None
                },
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(text) },
                enabled = text.isNotBlank(),
            ) { Text("确定") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
    )
}


// 可编辑的条目
@Composable
fun EditRow(title: String, content: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().wrapContentSize().clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = content,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Icon(Icons.Default.Edit, "", Modifier.size(16.dp))
            }
        }
    }
}
