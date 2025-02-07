package com.example.week03

import android.content.Intent
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

lateinit var txtUsername: EditText
lateinit var txtPassword: EditText
lateinit var btnLogin: Button

lateinit var viewModel: SampleViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.loginlayout)

        viewModel = ViewModelProvider(this).get(SampleViewModel::class.java)
        observeViewModel()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtUsername = findViewById(R.id.txtUsername)
        txtPassword = findViewById(R.id.txtPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener(this)

        Log.d("Checking", "onCreate Login Activity") // Log message to the Logcat
    }

    private fun observeViewModel() {
        viewModel.badgeCount.observe(this, Observer {
            showToast(it)
        })
    }

    private fun showToast(value: Int) {
        Toast.makeText(this, value.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onStart() { // Test lifecycle phases: onStart
        super.onStart()
        Log.d("Checking", "onStart Login Activity")
    }

    override fun onResume() { // Test lifecycle phases: onResume
        super.onResume()
        Log.d("Checking", "onResume Login Activity")
    }

    override fun onPause() { // Test lifecycle phases: onPause
        super.onPause()
        Log.d("Checking", "onPause Login Activity")
    }

    override fun onStop() { // Test lifecycle phases: onPause
        super.onStop()
        Log.d("Checking", "onStop Login Activity")
    }

    override fun onRestart() { // Test lifecycle phases: onRestart
        super.onRestart()
        Log.d("Checking", "onRestart Login Activity")
    }

    override fun onDestroy() { // Test lifecycle phases: onDestroy
        super.onDestroy()
        Log.d("Checking", "onDestroy Login Activity")
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnLogin-> {
                if(txtUsername.text.toString() == "kwan" && txtPassword.text.toString() == "123") {
                    viewModel.incrementBadgeCount()
                //                    val intent = Intent(this, AdminActivity::class.java)
//                    intent.putExtra("Username", txtUsername.text.toString())
//                    startActivity(intent)
                }
                else {
                    Toast.makeText(this, "Login failed!", Toast.LENGTH_LONG).show()
                }

            }
        }
    }
}