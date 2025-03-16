package com.example.lightmeup

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

lateinit var imgLike: ImageView


class MainActivity : AppCompatActivity(), View.OnLongClickListener {
    var likeStatus = "like";
    lateinit var model: ImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgLike = findViewById(R.id.imgLike)
        imgLike.setOnLongClickListener(this)

        model = ViewModelProvider(this).get(ImageViewModel::class.java)

        model.image.observe(this, Observer { image ->
            imgLike.setImageDrawable(getDrawable(image))
        })
    }

    override fun onLongClick(v: View?): Boolean {

        when(v?.id) { // switch case syntax in Kotlin
            R.id.imgLike -> {
               model.nextImage()
            }
        }

        return true
    }
}