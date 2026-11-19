package io.github.codedrink5154.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import io.github.codedrink5154.todoapp.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)

    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun login(name: String) {
        _currentUser.value = User(name, "")
    }

    fun updateName(newName: String) {
        _currentUser.update { user ->
            user?.copy(name = newName)
        }
    }

    fun logout() {
        _currentUser.value = null
    }
}
