package com.asadbek.todoappwithapi.myapi

import com.asadbek.todoappwithapi.models.DeleteTodo
import com.asadbek.todoappwithapi.models.MyToDo
import com.asadbek.todoappwithapi.models.NewTodo
import com.asadbek.todoappwithapi.models.Todo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MyApi {

    @GET("todos")
    fun getAllTodos():Call<MyToDo>
    @GET("todos/1")
    fun getATodo():Call<Todo>

    @FormUrlEncoded
    @POST("todos/add")
    suspend fun addTodo(
        @Field("todo") todo:String,
        @Field("completed") completed:Boolean,
        @Field("userId") userId:Int
    ):Response<Todo>

    @PUT("todos/{id}")
    suspend fun updateTodo(@Path("id") id:Int,@Body todo: NewTodo):Response<NewTodo>

    @DELETE("todos/{id}")
    suspend fun deleteTodo(@Path("id") id: Int):Response<DeleteTodo>
}