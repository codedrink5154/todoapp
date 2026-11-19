package io.github.codedrink5154.todoapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import io.github.codedrink5154.todoapp.data.User
import io.github.codedrink5154.todoapp.data.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel(
    private val prefsManager: PreferencesManager
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)

    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    /* 用于恢复之前的会话（即自动登录） */
    fun restoreSession() {
        val savedName: String? = prefsManager.loadSavedUserName()
        if (_currentUser.value != null || savedName.isNullOrBlank()) return
        _currentUser.value = User(savedName, "")
    }

    /* 保存当前用户的登录状态 */
    fun storeSession() {
        prefsManager.saveUserName(_currentUser.value?.name)
    }

    /* 登录，创建新的User对象 */
    fun login(name: String) {
        _currentUser.value = User(name, "")
        storeSession()
    }

    /* 更新当前用户的用户名，并且写入到登录凭据中 */
    fun updateName(newName: String) {
        _currentUser.update { user ->
            user?.copy(name = newName)
        }
        storeSession()
    }

    /* 退出登录,并且清空登录凭据 */
    fun logout() {
        _currentUser.value = null
        prefsManager.clearSavedUser()
    }

    init {
        restoreSession()
    }
}
