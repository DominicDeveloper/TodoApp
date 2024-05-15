package com.asadbek.todoappwithapi.models


data class Todo(
    val id: Int,
    val todo: String,
    val completed: Boolean,
    val userId: Int
)