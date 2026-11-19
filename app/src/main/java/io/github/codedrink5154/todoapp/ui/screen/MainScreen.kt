package io.github.codedrink5154.todoapp.ui.screen

import android.content.Context
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.ui.NavDisplay
import io.github.codedrink5154.todoapp.data.User
import io.github.codedrink5154.todoapp.navigation.Auth
import io.github.codedrink5154.todoapp.navigation.BackStack
import io.github.codedrink5154.todoapp.navigation.BottomNavBar
import io.github.codedrink5154.todoapp.navigation.Focus
import io.github.codedrink5154.todoapp.navigation.TodoList
import io.github.codedrink5154.todoapp.navigation.UserModify
import io.github.codedrink5154.todoapp.navigation.UserPage
import io.github.codedrink5154.todoapp.viewmodel.AppViewModel
import io.github.codedrink5154.todoapp.viewmodel.TodoViewModel

@ExperimentalMaterial3Api
@Composable
fun MainScreen(appViewModel: AppViewModel) {
    val context = LocalContext.current

    BackStack.stack = rememberNavBackStack(
        if (appViewModel.currentUser.value == null) Auth else TodoList
    )

    val currentTab = BackStack.getCurrent()

    Scaffold(
        topBar = {
            when (currentTab) {
                UserModify -> BackTopBar("修改资料")
                else ->
                    TopAppBar(
                        title = {
                            Text(
                                text = "AutoGTD",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        },
                    )
            }
        },
        bottomBar = {
            if (currentTab != null && currentTab != Auth && BackStack.getLength() <= 1) {
                BottomNavBar(currentTab)
            }
        },
    ) { innerPadding ->
        NavDisplay(
            backStack = BackStack.stack,
            onBack = { BackStack.goBack() },
            transitionSpec = {
                fadeIn(animationSpec = snap()) togetherWith fadeOut(animationSpec = snap())
            },
            popTransitionSpec = {
                fadeIn(animationSpec = snap()) togetherWith fadeOut(animationSpec = snap())
            },
            entryProvider = entryProvider {
                entry<Auth> { AuthScreen(innerPadding, onLoginSuccess = { name -> appViewModel.login(name) }) }
                entry<TodoList> {
                    val todoViewModel: TodoViewModel = viewModel()
                    TodoListScreen(innerPadding, todoViewModel)
                }
                entry<Focus> { FocusScreen(innerPadding) }
                entry<UserPage> {
                    val user by appViewModel.currentUser.collectAsStateWithLifecycle()
                    UserScreen(
                        innerPadding,
                        onLogout = { appViewModel.logout() },
                        userName = user?.name ?: "无"
                    )
                }
                entry<UserModify> {
                    val user by appViewModel.currentUser.collectAsStateWithLifecycle()
                    UserModify(
                        innerPadding,
                        userName = user?.name ?: "无",
                        changeName = { appViewModel.updateName(it) })
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(text: String) {
    TopAppBar(
        title = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
        },
        navigationIcon = {
            IconButton(onClick = { BackStack.goBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                )
            }
        },
    )
}
