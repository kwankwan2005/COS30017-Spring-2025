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

lateinit var txtView: TextView
lateinit var btnToast: Button

class Car(var brand: String, var model: String, var year: Int)
{

}

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtView = findViewById(R.id.txtView)
        btnToast = findViewById(R.id.btnToast)
        txtView.setText(R.string.test)

        btnToast.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnToast-> {
                btnToast.setText("Test clicked!")
                Toast.makeText(this, "Hello world", Toast.LENGTH_LONG).show()
            }
        }
    }
}