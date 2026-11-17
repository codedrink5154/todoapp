package io.github.codedrink5154.todoapp.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data object AppState {
    var user by mutableStateOf(User("", ""))
}
