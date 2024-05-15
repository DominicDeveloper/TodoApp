package com.asadbek.todoappwithapi

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.asadbek.todoappwithapi.databinding.ActivitySingleBinding
import com.asadbek.todoappwithapi.models.Todo
import com.asadbek.todoappwithapi.myapi.MyApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SingleActivity : AppCompatActivity() {
    private val BASE_URL = "https://dummyjson.com/"
    lateinit var binding: ActivitySingleBinding
    lateinit var listTodo:ArrayList<String>
  override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listTodo = ArrayList()


      getATodo()

    }

    private fun getATodo(){
        binding.progressBar.visibility = View.VISIBLE
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)
        api.getATodo().enqueue(object :Callback<Todo>{
            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        binding.progressBar.visibility = View.GONE
                        binding.txtName.text = it.id.toString()
                        binding.txtDescription.text = it.todo
                    }
                }
            }

            override fun onFailure(call: Call<Todo>, t: Throwable) {

            }

        })
    }
}