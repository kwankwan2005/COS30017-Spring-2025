package com.example.lecturew06

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_fragment_layout)
    }

    fun showText(firstName: String, lastName: String) {
        val bottomFragment = supportFragmentManager.findFragmentById(R.id.fragment_bottom) as BottomFragment
        bottomFragment.showText(firstName, lastName)
    }
}