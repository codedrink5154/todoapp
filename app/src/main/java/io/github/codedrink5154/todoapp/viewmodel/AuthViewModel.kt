package io.github.codedrink5154.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {
    private val _isRegister = MutableStateFlow(false)

    private var _name = MutableStateFlow("")
    private var _password = MutableStateFlow("")
    private var _repeatPassword = MutableStateFlow("")

    val isRegister: StateFlow<Boolean> = _isRegister.asStateFlow()

    val name: StateFlow<String> = _name.asStateFlow()
    val password: StateFlow<String> = _password.asStateFlow()
    val repeatPassword: StateFlow<String> = _repeatPassword.asStateFlow()

    fun updateName(name: String) {
        _name.update { name }
    }

    fun updatePassword(passwd: String) {
        _password.update { passwd }
    }

    fun updateRepeatPassword(passwd: String) {
        _repeatPassword.update { passwd }
    }

    fun switchRegisterState() {
        _isRegister.update { !it }
    }

    // 注册新的用户
    fun registerNewAccount(name: String, passwd: String): Boolean {
        TODO("需要更加完善的注册机制, 而不是使用sharedPreferences")
    }

    // 登录,返回一个Boolean，表示登录是否成功
    // TODO
    fun tryLogin(name: String, password: String): Boolean {
        return true
    }
}
