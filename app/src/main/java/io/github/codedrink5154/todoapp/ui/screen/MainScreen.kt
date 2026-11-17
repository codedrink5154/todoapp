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
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import io.github.codedrink5154.todoapp.data.AppState
import io.github.codedrink5154.todoapp.data.User
import io.github.codedrink5154.todoapp.navigation.Auth
import io.github.codedrink5154.todoapp.navigation.BackStack
import io.github.codedrink5154.todoapp.navigation.BottomNavBar
import io.github.codedrink5154.todoapp.navigation.Focus
import io.github.codedrink5154.todoapp.navigation.TodoList
import io.github.codedrink5154.todoapp.navigation.UserModify
import io.github.codedrink5154.todoapp.navigation.UserPage

@ExperimentalMaterial3Api
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val lastUser = remember {
        context.getSharedPreferences("lastUser", Context.MODE_PRIVATE)
            .getString("lastUser", null)
    }

    // 首次进入时恢复当前用户，避免每次重组都覆盖 AppState
    LaunchedEffect(lastUser) {
        AppState.user = User(lastUser.orEmpty(), "")
    }

    // lastUser 为 null 或空串时进入登录页
    BackStack.stack = rememberNavBackStack(
        if (lastUser.isNullOrBlank()) Auth else TodoList
    )

    val current = BackStack.getCurrent()

    Scaffold(
        topBar = {
            if (current == UserModify) {
                BackTopBar()
            } else {
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
            if (current != null && current != Auth && BackStack.getLength() <= 1) {
                BottomNavBar(current)
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
                entry<Auth> { AuthScreen(innerPadding) }
                entry<TodoList> { TodoListScreen(innerPadding) }
                entry<Focus> { FocusScreen(innerPadding) }
                entry<UserPage> { UserScreen(innerPadding) }
                entry<UserModify> { UserModify(innerPadding) }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "编辑资料",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
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
