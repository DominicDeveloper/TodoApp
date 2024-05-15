package com.asadbek.todoappwithapi.models

data class MyToDo(
    val todos: List<Todo>,
    val limit: Int,
    val skip: Int,
    val total: Int
)