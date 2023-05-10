package com.example.carrental.booking

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.carrental.DataBaseHelper
import com.example.carrental.R
import com.google.android.material.snackbar.Snackbar

class EditBookingActivity : AppCompatActivity() {

    private lateinit var bookingIdTextView: TextView
    private lateinit var carModelEditText: EditText
    private lateinit var pickupDateEditText: EditText
    private lateinit var returnDateEditText: EditText
    private lateinit var durationEditText: EditText
    private lateinit var insuranceEditText: EditText
    private lateinit var gpsEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_booking)

        bookingIdTextView = findViewById(R.id.booking_id_text_view)
        carModelEditText = findViewById(R.id.car_model_edit_text)
        pickupDateEditText = findViewById(R.id.pickup_date_edit_text)
        returnDateEditText = findViewById(R.id.return_date_edit_text)
        durationEditText = findViewById(R.id.duration_edit_text)
        insuranceEditText = findViewById(R.id.insurance_edit_text)
        gpsEditText = findViewById(R.id.gps_edit_text)
        saveButton = findViewById(R.id.save_button)
        cancelButton = findViewById(R.id.cancel_button)

        val bookingId = intent.getIntExtra("booking_id", -1)
        val userId = intent.getIntExtra("user_id", -1)
        val db = DataBaseHelper(this)
        val booking = db.getUserBookingById(bookingId)

        if (booking != null) {
            bookingIdTextView.text = booking.booking_id.toString()
            carModelEditText.setText(booking.model)
            pickupDateEditText.setText(booking.pickupDate)
            returnDateEditText.setText(booking.returnDate)
            durationEditText.setText(booking.duration)
            insuranceEditText.setText(booking.extra1)
            gpsEditText.setText(booking.extra2)
        }

        saveButton.setOnClickListener {
            val carModel = carModelEditText.text.toString()
            val pickupDate = pickupDateEditText.text.toString()
            val returnDate = returnDateEditText.text.toString()
            val duration = durationEditText.text.toString()
            val insurance = insuranceEditText.text.toString()
            val gps = gpsEditText.text.toString()

            val updatedBooking = Booking(bookingId, userId, carModel, pickupDate, returnDate, duration, insurance, gps)

            if (db.updateBooking(updatedBooking)) {
                val intent = Intent(this, BookingActivity::class.java)
                startActivity(intent)
            } else {
                Snackbar.make(it, "Failed to update booking", Snackbar.LENGTH_SHORT).show()
            }
        }

            cancelButton.setOnClickListener {
                val intent = Intent(this, BookingActivity::class.java)
                startActivity(intent)
            }
        }
    }


