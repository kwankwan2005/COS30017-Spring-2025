package com.example.multiple

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(val opResult: Int): Parcelable {
}