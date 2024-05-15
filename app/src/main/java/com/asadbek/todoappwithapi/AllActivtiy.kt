package com.asadbek.todoappwithapi

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.asadbek.todoappwithapi.adapters.TodoAllAdapter
import com.asadbek.todoappwithapi.databinding.ActivityAllActivtiyBinding
import com.asadbek.todoappwithapi.databinding.DialogTodoBinding
import com.asadbek.todoappwithapi.models.MyToDo
import com.asadbek.todoappwithapi.models.NewTodo
import com.asadbek.todoappwithapi.models.Todo
import com.asadbek.todoappwithapi.myapi.MyApi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class AllActivtiy : AppCompatActivity() {
    private val BASE_URL = "https://dummyjson.com/"
    private val TAG = "CHECK_RESPONE"
    private val TAG2 = "ISSAVED"
    var condition = "Saved!"
    var n:String? = null
    lateinit var todoAllAdapter: TodoAllAdapter
    lateinit var listTodo:ArrayList<Todo>
    lateinit var binding: ActivityAllActivtiyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllActivtiyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listTodo = ArrayList()
        getAllTodos()




        justBePatently()

        binding.btnAdd.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val dialogTodoBinding = DialogTodoBinding.inflate(layoutInflater)
            dialog.setView(dialogTodoBinding.root)
            dialog.show()
            dialogTodoBinding.dialogToDoBtn.setOnClickListener {
                val todo = dialogTodoBinding.dialogTodoDescription.text.toString().trim()
                val id = listTodo.last().id+1
                val isCompleted = false
                val userId = listTodo.last().userId+1
                addTodo(id,todo,isCompleted,userId)
                dialog.dismiss()
                aWait()
            }


        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun aWait() {
        binding.progressBar.visibility = View.VISIBLE
        val handler = Handler()
        handler.postDelayed({
                            binding.progressBar.visibility = View.GONE
            if (n!!.isNotEmpty()){
                binding.txtNew.visibility = View.VISIBLE
                Toast.makeText(this, condition, Toast.LENGTH_SHORT).show()
                binding.txtNew.text = n
                todoAllAdapter.notifyDataSetChanged()
            }
        },2000)
    }

    private fun justBePatently() {
        binding.progressBar.visibility = View.VISIBLE
        val handler = Handler()
        handler.postDelayed({
            listTodo.sortedBy { it.id }
            todoAllAdapter = TodoAllAdapter(listTodo,object :TodoAllAdapter.RvClick{
                override fun delete(toDo: Todo) {
                    deleteDialog(toDo)
                }

                override fun edit(toDo: Todo) {
                    editDialog(toDo)
                }

            })

            binding.rv.adapter = todoAllAdapter
          //  todoAllAdapter.notifyDataSetChanged()
            binding.progressBar.visibility = View.INVISIBLE
        },2000)
    }

    private fun deleteDialog(toDo: Todo) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setMessage("Are you sure you want to delete this todo?")
        alertDialog.setTitle("Todo")
        alertDialog.setCancelable(true)
        alertDialog.setNegativeButton("No",object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }
        })
        alertDialog.setPositiveButton("Yes",object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                deleteTodo(toDo)
                aWait()
            }
        })
        alertDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun editDialog(toDo: Todo) {
        var iscompleted = false
        val dialog = AlertDialog.Builder(this).create()
        val dialogTodoBinding = DialogTodoBinding.inflate(layoutInflater)
        dialog.setView(dialogTodoBinding.root)
        dialogTodoBinding.dialogTodoName.setText(toDo.id.toString())
        dialogTodoBinding.dialogTodoDescription.setText(toDo.todo.toString())
        dialogTodoBinding.dialogToDoBtn.setText("UPDATE")
        dialogTodoBinding.dialogWarning.visibility = View.VISIBLE
        dialog.show()
        dialogTodoBinding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) iscompleted = true
            else iscompleted = false
        }
        dialogTodoBinding.dialogToDoBtn.setOnClickListener {
            Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT).show()
            val id = dialogTodoBinding.dialogTodoName.text.toString().toInt()
            val td = dialogTodoBinding.dialogTodoDescription.text.toString().trim()
            val completed = iscompleted
            val userId = toDo.userId
            editTodo(id.toString(),td,completed,userId)
            dialog.dismiss()
            aWait()

        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun editTodo(id: String, todo: String, completed: Boolean, userId: Int){
        GlobalScope.launch(Dispatchers.IO){
            val response = try {
                RetrofitInstance.api.updateTodo(id.toInt(), NewTodo(id,todo,completed,userId))
            }catch (e:HttpException){
                Toast.makeText(this@AllActivtiy, "Error by http", Toast.LENGTH_SHORT).show()
                return@launch
            }catch (e:IOException){
                Toast.makeText(this@AllActivtiy, "App error", Toast.LENGTH_SHORT).show()
                return@launch
            }
            if (response.isSuccessful){
               condition = "Updated!"
                Log.i(TAG, "editTodo: ${response.body()}")
               n = "Id: ${response.body()!!.id}\ntodo: ${response.body()!!.todo}\ncompleted: ${response.body()!!.completed}\nuserId: ${response.body()!!.userId}"
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun addTodo(id:Int, todo:String, completed:Boolean, userId:Int){
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.addTodo(todo,completed,userId)

            }catch (e:HttpException){
                Toast.makeText(this@AllActivtiy,"Error by http ${e.message}",Toast.LENGTH_SHORT).show()
                return@launch
            }catch (e:IOException){
                Toast.makeText(this@AllActivtiy, "App error", Toast.LENGTH_SHORT).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null){
                condition = "Saved!"
                n = "${response.body()!!.id}\n${response.body()!!.todo}\n${response.body()!!.completed}\n${response.body()!!.userId}"
                listTodo.add(Todo(response.body()!!.id,response.body()!!.todo,response.body()!!.completed,response.body()!!.userId))
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun deleteTodo(todo: Todo){
        GlobalScope.launch(Dispatchers.IO){
            val response = try {
                RetrofitInstance.api.deleteTodo(todo.id)
            }catch (e:IOException){
                Toast.makeText(this@AllActivtiy, "App error", Toast.LENGTH_SHORT).show()
                return@launch
            }

            if (response.isSuccessful){
                condition = "Deleted!"
                n = "Id: ${response.body()!!.id}\nIs delete: ${response.body()!!.isDeleted}"
                listTodo.remove(todo)
            }
        }
    }

    private fun getAllTodos(){
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)
        api.getAllTodos().enqueue(object :Callback<MyToDo>{
            override fun onResponse(call: Call<MyToDo>, response: Response<MyToDo>) {
                // 
                if (response.isSuccessful){
                    response.body()?.let { 
                        for (todos in it.todos){
                            listTodo.add(Todo(todos.id,todos.todo,todos.completed,todos.userId))
                            Log.i(TAG, "onResponse: ${todos.todo}")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MyToDo>, t: Throwable) {
                Log.i(TAG, "onFailure: ${t.message}")
            }
        })
    }


}

