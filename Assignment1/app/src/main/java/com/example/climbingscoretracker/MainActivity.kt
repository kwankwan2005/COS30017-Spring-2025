package com.example.climbingscoretracker

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var txtScoreNumber: TextView
    lateinit var txtCurrentHold: TextView
    lateinit var btnClimb: Button
    lateinit var btnFall: Button
    lateinit var btnReset: Button

    lateinit var viewModel: ScoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("TrackerScreen", "Screen initialized")

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this).get(ScoreViewModel::class.java)
        subscribeEventViewModel()

        txtScoreNumber = findViewById(R.id.txtScoreNumber)
        txtCurrentHold = findViewById(R.id.txtCurrentHold)
        btnClimb = findViewById(R.id.btnClimb)
        btnFall = findViewById(R.id.btnFall)
        btnReset = findViewById(R.id.btnReset)

        btnClimb.setOnClickListener(this)
        btnFall.setOnClickListener(this)
        btnReset.setOnClickListener(this)

        Log.d("TrackerScreen", "Completed setting up the buttons for interaction")
    }

    // Handle UI view
    private fun subscribeEventViewModel() {
        viewModel.scoreTracker.observe(this, Observer {
            Log.d("Screen", "Receive new data from the ViewModel")

            txtScoreNumber.setText(it.getScore().toString())

            var currentHold = it.getHold()

            // Display score color based on current hold
            when (currentHold) {
                in 1..3 -> {
                    txtScoreNumber.setTextColor(Color.rgb(45,38,145))
                }
                in 4..6 -> {
                    txtScoreNumber.setTextColor(Color.rgb(38,145,74))
                }
                in 7..9 -> {
                    txtScoreNumber.setTextColor(Color.rgb(168,50,50))
                }
                else -> {
                    txtScoreNumber.setTextColor(Color.rgb(0,0,0))
                }
            }

            // Display hold text
            if(currentHold == 0) {
                txtCurrentHold.setText(getString(R.string.lblNotClimbed))
            }
            else {
                txtCurrentHold.setText(getString(R.string.lblCurrentHold) + " " + it.getHold().toString())
            }

            // Disabled button
            btnFall.isEnabled = it.availableToFall()
            btnClimb.isEnabled = it.availableToClimb()
        })
        Log.d("TrackerScreen", "Subscribed to changes in ViewModel")
    }

    // Handle interaction
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnClimb-> {
                Log.d("TrackerScreen", "Climb button clicked")
                viewModel.climb()
            }
            R.id.btnFall-> {
                Log.d("TrackerScreen", "Fall button clicked")
                viewModel.fall()
            }
            R.id.btnReset-> {
                Log.d("TrackerScreen", "Reset button clicked")
                viewModel.reset()
            }
        }
    }
}