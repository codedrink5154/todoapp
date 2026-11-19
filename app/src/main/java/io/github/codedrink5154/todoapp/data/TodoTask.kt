package io.github.codedrink5154.todoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class TodoTask(
    val id: Long,
    val title: String,
    val category: String?,
    val description: String = "",
    val deadline: Long?,
    val remind: Long?,
    val done: Boolean = false,
    val createdAt: Long,
)
