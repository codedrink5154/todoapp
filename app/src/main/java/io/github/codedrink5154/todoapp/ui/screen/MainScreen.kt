package io.github.codedrink5154.todoapp.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entryProvider
import androidx.navigationevent.compose.NavigationEventDispatcherOwnerHostDefaultKey
import io.github.codedrink5154.todoapp.navigation.Auth
import io.github.codedrink5154.todoapp.navigation.BottomNavBar
import io.github.codedrink5154.todoapp.navigation.TodoList
import io.github.codedrink5154.todoapp.navigation.Focus
import io.github.codedrink5154.todoapp.navigation.User
import io.github.codedrink5154.todoapp.ui.theme.TodoAppTheme
import io.github.codedrink5154.todoapp.ui.screen.AuthScreen
import io.github.codedrink5154.todoapp.ui.screen.FocusScreen
import kotlin.experimental.ExperimentalNativeApi

@ExperimentalMaterial3Api
@Composable
fun MainScreen(user: String?) {

    // 若user为null，说明没有保存登录状态，进入登录界面
    // 反之，则直接进入主界面
    val backStack = if (user == null) rememberNavBackStack(Auth) else rememberNavBackStack(TodoList)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("codedrink5154's Todo App") }
            )
        },
        bottomBar = {
            if (user != null) {
                BottomNavBar(backStack.lastOrNull() ?: TodoList)
            }
        }
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<Auth> { AuthScreen() }
                entry<TodoList> { TodoListScreen() }
                entry<Focus> { FocusScreen() }
            }
        )
    }
}
