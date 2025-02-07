package com.example.climbingscoretracker

import android.content.Intent
import android.os.Bundle
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
    }

    private fun subscribeEventViewModel() {
        viewModel.scoreTracker.observe(this, Observer {
            txtScoreNumber.setText(it.getScore().toString())

            // Display hold text
            if(it.getHold() == 0) {
                txtCurrentHold.setText(getString(R.string.lblNotClimbed))
            }
            else {
                txtCurrentHold.setText(getString(R.string.lblCurrentHold) + " " + it.getHold().toString())
            }

            // Disabled button
            btnFall.isEnabled = it.availableToFall()
            btnClimb.isEnabled = it.availableToClimb()

        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnClimb-> {
                viewModel.climb()
            }
            R.id.btnFall-> {
                viewModel.fall()
            }
            R.id.btnReset-> {
                viewModel.reset()
            }
        }
    }
}