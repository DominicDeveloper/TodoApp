package com.asadbek.todoappwithapi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asadbek.todoappwithapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetAllTodo.setOnClickListener {
            forIntent(AllActivtiy::class.java)
        }
        binding.btnGetASingleTodo.setOnClickListener {
            forIntent(SingleActivity::class.java)
        }

    }

    private fun forIntent(cls:Class<*>){
        val intent = Intent(this,cls)
        startActivity(intent)
    }
}