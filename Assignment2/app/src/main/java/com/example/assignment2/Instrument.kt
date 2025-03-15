package com.example.assignment2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Instrument(val name: String, val image: Int, val price: Float, var rating: Float, var rented: Boolean, val tags: MutableList<String>, val accessories: MutableList<String>, var rentedAccessories: MutableList<String>) :
    Parcelable
