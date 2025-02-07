package com.example.week03

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

lateinit var btnLanguage: Button
lateinit var txtAdmin: TextView

var language: String = "VN"

class AdminActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.adminlayout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d("AdminChecking", "onCreate Admin Activity")

        txtAdmin = findViewById(R.id.txtAdmin)

        val intent = getIntent()
        val username = intent.getStringExtra("Username")

        txtAdmin.setText("Good afternoon, " + username + "!")

        btnLanguage = findViewById(R.id.btnLanguage)
        btnLanguage.setText(R.string.VN_TEXT)
        btnLanguage.setOnClickListener(this)
    }

    override fun onStart() { // Test lifecycle phases: onStart
        super.onStart()
        Log.d("AdminChecking", "onStart Admin Activity")
    }

    override fun onResume() { // Test lifecycle phases: onResume
        super.onResume()
        Log.d("AdminChecking", "onResume Admin Activity")
    }

    override fun onPause() { // Test lifecycle phases: onPause
        super.onPause()
        Log.d("AdminChecking", "onPause Admin Activity")
    }

    override fun onStop() { // Test lifecycle phases: onPause
        super.onStop()
        Log.d("AdminChecking", "onStop Admin Activity")
    }

    override fun onRestart() { // Test lifecycle phases: onRestart
        super.onRestart()
        Log.d("AdminChecking", "onRestart Admin Activity")
    }

    override fun onDestroy() { // Test lifecycle phases: onDestroy
        super.onDestroy()
        Log.d("AdminChecking", "onDestroy Admin Activity")
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnLanguage-> {
                finish()
            }
        }
    }
}