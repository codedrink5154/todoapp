package io.github.codedrink5154.todoapp.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Timer


@Composable
fun BottomNavBar(currentTab: NavKey) {
    NavigationBar {
        NavigationBarItem(
            selected = currentTab == TodoList,
            onClick = { BackStack.switchTab(TodoList) },
            label = { Text("待办") },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "待办") }
        )
        NavigationBarItem(
            selected = currentTab == Focus,
            onClick = { BackStack.switchTab(Focus) },
            label = { Text("专注") },
            icon = { Icon(Icons.Default.Timer, contentDescription = "专注") }
        )
        NavigationBarItem(
            selected = currentTab == UserPage,
            onClick = { BackStack.switchTab(UserPage) },
            label = { Text("我的") },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "我的") }
        )
    }
}
