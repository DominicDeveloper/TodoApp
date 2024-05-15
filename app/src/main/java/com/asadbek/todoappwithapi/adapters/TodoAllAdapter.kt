package com.asadbek.todoappwithapi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asadbek.todoappwithapi.databinding.ItemTodoBinding
import com.asadbek.todoappwithapi.models.Todo

class TodoAllAdapter(var list:List<Todo>, val rvClick: RvClick): RecyclerView.Adapter<TodoAllAdapter.Vh>() {
        inner class Vh(var itemTodoBinding: ItemTodoBinding): RecyclerView.ViewHolder(itemTodoBinding.root){

            fun onBind(toDo: Todo, position: Int){
                itemTodoBinding.itemTodoName.text = toDo.id.toString()
                itemTodoBinding.itemTodoDescription.text = toDo.todo.toString()
                itemTodoBinding.itemTodoCondition.text = toDo.completed.toString()

                itemTodoBinding.itemToDoDelete.setOnClickListener {
                    rvClick.delete(toDo)
                }
                itemTodoBinding.itemTodoEdit.setOnClickListener {
                    rvClick.edit(toDo)
                }


            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
            return Vh(ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: Vh, position: Int) {
            holder.onBind(list[position], position)
        }

        override fun getItemCount(): Int = list.size

        interface RvClick{
            fun delete(toDo: Todo)
            fun edit(toDo: Todo)
        }
}