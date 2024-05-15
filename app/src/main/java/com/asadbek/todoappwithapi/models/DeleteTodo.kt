package com.asadbek.todoappwithapi.models

data class DeleteTodo(
    val completed: Boolean,
    val deletedOn: Boolean,
    val id: Int,
    val isDeleted: Boolean,
    val todo: String,
    val userId: Int
)