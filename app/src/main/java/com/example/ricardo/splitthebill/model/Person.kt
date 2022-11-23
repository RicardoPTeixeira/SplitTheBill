package com.example.ricardo.splitthebill.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Person(
    val id: Int,
    var name: String,
    var spent: String,
    var debt: String,
    var discription: String,
): Parcelable