package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

lateinit var txtNum1: EditText
lateinit var txtNum2: EditText
lateinit var btnCalc: Button
lateinit var txtResult: TextView
lateinit var radioAdd: RadioButton
lateinit var radioMult: RadioButton

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var operator = "plus";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtNum1 = findViewById(R.id.txtNum1)
        txtNum2 = findViewById(R.id.txtNum2)
        btnCalc = findViewById(R.id.btnCalc)
        txtResult = findViewById(R.id.txtResult)

        radioAdd = findViewById(R.id.radioPlus)
        radioMult = findViewById(R.id.radioMult)

        btnCalc.setOnClickListener(this)

        radioAdd.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                operator = "plus"
            }
        }

        radioMult.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                operator = "mult"
            }
        }
    }

    fun add(num1: Int, num2: Int): Int {
        return num1 + num2
    }

    fun mult(num1: Int, num2: Int): Int {
        return num1 * num2
    }

    override fun onClick(v: View?) {
        when(v?.id) { // switch case syntax in Kotlin
            R.id.btnCalc->{
                if(txtNum1.text.toString() == "" || txtNum2.text.toString() == "") {
                    txtResult.setText("Please enter numbers")
                }
                else {
                    when(operator) {
                        "plus" -> txtResult.setText(add(txtNum1.text.toString().toInt(), txtNum2.text.toString().toInt()).toString())
                        "mult" -> txtResult.setText(mult(txtNum1.text.toString().toInt(), txtNum2.text.toString().toInt()).toString())
                    }
                }
            }
        }
    }
}