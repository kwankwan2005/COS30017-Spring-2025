package com.example.week03

import android.os.Bundle
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

        btnLanguage = findViewById(R.id.btnLanguage)
        btnLanguage.setText(R.string.VN_TEXT)
        btnLanguage.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnLanguage-> {
                if(language == "EN") {
                    language = "VN"
                    btnLanguage.setText(R.string.VN_TEXT)
                }
                else {
                    language = "EN"
                    btnLanguage.setText(R.string.EN_TEXT)
                }
                Toast.makeText(this, "Change language successfully!", Toast.LENGTH_LONG).show()
            }
        }
    }
}