package io.github.codedrink5154.todoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class TodoTask(
    val id: Int,
    val title: String,
    val category: String?,
    val description: String = "",
    val deadline: Long?,
    val remind: Int?, // 在remind分钟前提醒
    val done: Boolean = false,
    val createdAt: Long,
)
