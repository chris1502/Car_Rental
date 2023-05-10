package com.example.carrental.booking

import java.io.Serializable

data class Booking(
    val booking_id: Int,
    val userId: Int,
    val model: String,
    val pickupDate: String,
    val returnDate: String?,
    val duration: String,
    val extra1: String?,
    val extra2: String?
) : Serializable

