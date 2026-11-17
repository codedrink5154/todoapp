package io.github.codedrink5154.todoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val password: String,
)
