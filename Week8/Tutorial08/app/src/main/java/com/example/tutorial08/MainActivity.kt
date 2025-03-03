package com.example.tutorial08

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lecture07.RecyclerViewAdapter
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var RecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        RecyclerView = findViewById(R.id.lineView)
        RecyclerView.layoutManager = LinearLayoutManager(this)
        RecyclerView.setHasFixedSize(true)

        val data = ArrayList<ListItemDouble>()

        resources.openRawResource(R.raw.dummy).bufferedReader().forEachLine {
            val text = it.split(",")
            data.add(ListItemDouble(text[0],text[1]))
        }


        val adapter = RecyclerViewAdapter(data)
        RecyclerView.setAdapter(adapter)

        Log.d("Test", adapter.itemCount.toString())
    }
}