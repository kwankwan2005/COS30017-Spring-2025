package com.example.lecture02demo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

lateinit var txtNum1: EditText
lateinit var txtNum2: EditText
lateinit var btnPlus: Button
lateinit var btnMinus: Button
lateinit var btnMultiply: Button
lateinit var btnDivide: Button
lateinit var txtResult: TextView

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

        txtNum1 = findViewById(R.id.txtNum1)
        txtNum2 = findViewById(R.id.txtNum2)
        btnPlus = findViewById(R.id.btnPlus)
        btnMinus = findViewById(R.id.btnMinus)
        btnMultiply = findViewById(R.id.btnMultiply)
        btnDivide = findViewById(R.id.btnDivide)
        txtResult = findViewById(R.id.txtResult)

        btnPlus.setOnClickListener(this)
        btnMinus.setOnClickListener(this)
        btnMultiply.setOnClickListener(this)
        btnDivide.setOnClickListener(this)

        var res = ""
        // Single iteration loop syntax
//        for(i in 1..3) {
//            res += i
//        }

        // Iteration and break loop
        var cars = arrayOf("Volvo", "Huyndai", "Toyota")
        checkpoint@ for (i in cars.indices) {
            if(cars.get(i) == "Huyndai") continue@checkpoint
            res += cars.get(i)
        }

        txtResult.setText(res)

        val c1 = Car("Ford", "Mustang", 1969)
        txtResult.setText((c1.brand))
    }

    fun calculateNumber(mode: String?, num1: String?, num2: String?): String {
        var str = "";
        if(num1 == "" || num2 == "") {
            str = "Please enter two numbers!"
            return str
        }

        var number1 = num1.toString().toDouble();
        var number2 = num2.toString().toDouble();

        if(mode == "+") {
            var result = number1 + number2;
            str = "Result is " + result.toString()
        }
        if(mode == "-") {
            var result = number1 - number2;
            str = "Result is " + result.toString()
        }
        if(mode == "*") {
            var result = number1 * number2;
            str = "Result is " + result.toString()
        }
        if(mode == "/") {
            if(number2 == 0.0) {
                str = "You can't divide by 0"
            }
            else {
                var result = number1 / number2;
                str = "Result is " + result.toString()
            }
        }
        return str
    }

    override fun onClick(v: View?) {


        when(v?.id) { // switch case syntax in Kotlin
            R.id.btnPlus->{
                txtResult.setText(calculateNumber("+", txtNum1.text.toString(), txtNum2.text.toString()));
            }
            R.id.btnMinus->{
                txtResult.setText(calculateNumber("-", txtNum1.text.toString(), txtNum2.text.toString()));
            }
            R.id.btnMultiply->{
                txtResult.setText(calculateNumber("*", txtNum1.text.toString(), txtNum2.text.toString()));
            }
            R.id.btnDivide->{
                txtResult.setText(calculateNumber("/", txtNum1.text.toString(), txtNum2.text.toString()));

            }
        }
    }
}