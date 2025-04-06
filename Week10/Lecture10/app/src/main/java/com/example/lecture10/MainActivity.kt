package com.example.lecture10

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.widget.TextView

class MainActivity : AppCompatActivity(), SensorEventListener {
    lateinit var mSensorManager: SensorManager
    lateinit var txtSensor: TextView

    var mSensors: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val deviceSensors: List<Sensor> = mSensorManager.getSensorList(Sensor.TYPE_ALL)
        Log.v("Sensors", "Total sensors: " + deviceSensors.size)
        deviceSensors.forEach {
            Log.v("Sensors", "Sensor names: " + it)
        }

        mSensors = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        txtSensor = findViewById(R.id.txtSensorvalues)
    }

    override fun onResume() {
        super.onResume()

        mSensorManager.registerListener(this, mSensors, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val millibarsOfPressure = event!!.values[0]
        if(event.sensor.type == Sensor.TYPE_LIGHT)
            txtSensor.setText(millibarsOfPressure.toString() + "kt")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}