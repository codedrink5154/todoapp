package io.github.codedrink5154.todoapp.navigation

import kotlinx.serialization.Serializable
import androidx.navigation3.runtime.NavKey

// 定义页面
@Serializable
data object TodoList : NavKey

@Serializable
data object Focus : NavKey

@Serializable
data object Auth : NavKey

@Serializable
data object User : NavKey
