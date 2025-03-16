package com.example.lightmeup

import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageViewModel : ViewModel() {

    var image = MutableLiveData<Int>()

    init {
        image.value = R.drawable.thumb_down_24dp_5f6368
    }



    fun nextImage() {
        when(image.value) {
            R.drawable.thumb_down_24dp_5f6368 -> {
                image.value = R.drawable.thumb_up_24dp_5f6368
            }
            R.drawable.thumb_up_24dp_5f6368 -> {
                image.value = R.drawable.thumb_down_24dp_5f6368
            }
        }
    }

    // Rest of the ViewModel...
}