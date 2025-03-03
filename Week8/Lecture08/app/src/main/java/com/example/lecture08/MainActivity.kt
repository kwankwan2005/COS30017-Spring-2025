package com.example.lecture08

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var btnSave: Button
    lateinit var btnRead: Button
    lateinit var txtResult: TextView
    lateinit var txtEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        btnSave = findViewById(R.id.btnSave)
        txtEdit = findViewById(R.id.txtEdit)
        btnRead = findViewById(R.id.btnRead)
        txtResult = findViewById(R.id.txtResult)
        btnSave.setOnClickListener(this)
        btnRead.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {

            R.id.btnSave -> {
                // Internal save
//                var file = openFileOutput("test.txt", Context.MODE_PRIVATE)
//                var message = txtEdit.text.toString()
//                file.write(message.toByteArray())
//                file.close()

                // External save
                val file = File(Environment.getExternalStorageDirectory(), "/Documents/test.txt")
                file.createNewFile()
                file.writeText("Hello external storage!")

                Toast.makeText(this, "Save successfully", Toast.LENGTH_SHORT).show()
            }


            R.id.btnRead -> {

                // Internal read - Way #1
//                var file = openFileInput("test.txt")
//                val data = ByteArray(1024)
//                file.read(data)
//                txtResult.text = data.toString(Charsets.UTF_8)
//                file.close()

                // Internal read - Way #2
//                val file = File(applicationContext.filesDir, "test.txt")
//                val contents = file.readText()
//                txtResult.text = contents.toString()




                // External read
                val file_b = File(Environment.getExternalStorageDirectory(), "/Documents/test2.txt").exists()
                if(file_b) {
                    val file = File(Environment.getExternalStorageDirectory(), "/Documents/test2.txt")
                    txtResult.text = file.readText()
                }
                else {
                    Toast.makeText(this, "404: File not found.", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}