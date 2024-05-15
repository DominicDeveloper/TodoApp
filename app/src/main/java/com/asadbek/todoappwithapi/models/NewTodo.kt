package com.asadbek.todoappwithapi.models

// this class uses for updating todos
data class NewTodo(
    val id: String,
    val todo: String,
    val completed: Boolean,
    val userId: Int
)