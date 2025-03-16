package com.example.multiple

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi

class ResultActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val result = intent.getParcelableExtra<Result>("result", Result::class.java)

        val tvResult = findViewById<TextView>(R.id.result)
        tvResult.text = result?.opResult.toString()

    }
}