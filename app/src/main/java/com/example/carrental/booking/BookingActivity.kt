package com.example.carrental.booking

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.DatePicker
import com.example.carrental.*
import com.example.carrental.adapters.Car
import com.example.carrental.adapters.CarAdapter
import com.example.carrental.user.UserEdit
import kotlin.random.Random



class BookingActivity : AppCompatActivity() {

    private var userId: Int = 1
    private var model: String = ""
    private var pickupDate: String = ""
    private var duration: String? = null
    private var extra1: String? = null
    private var extra2: String? = null
    private var selectedCar: Car? = null


    private fun generateUniqueBookingId(userId: Int): String {
        val timestamp = System.currentTimeMillis()
        val randomInt = Random.nextInt(1, 9999)
        return "$userId$timestamp$randomInt"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        // Get the user ID from the intent
        userId = intent.getIntExtra("userId", -1)

        // Get the car model and pickup date from the intent
        model = intent.getStringExtra("model") ?: ""
        pickupDate = intent.getStringExtra("pickupDate") ?: ""


        val dbHelper = DataBaseHelper(this)
        val cars = dbHelper.getAllCars()

        // Set up the RecyclerView
        val carRecyclerView = findViewById<RecyclerView>(R.id.car_recycler_view)
        val adapter = CarAdapter(cars)
        carRecyclerView.adapter = adapter
        carRecyclerView.layoutManager = LinearLayoutManager(this)

        // Handle item click events
        // Handle item click events
        adapter.setOnItemClickListener { car ->
            selectedCar = car
        }
        val brandSpinner = findViewById<Spinner>(R.id.brand_spinner)
        brandSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedBrand = parent?.getItemAtPosition(position) as String
                // Call a function to filter the RecyclerView based on the selected brand, passing the context from the view parameter
                filterRecyclerView(view?.context, selectedBrand)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }

            private fun filterRecyclerView(context: Context?, brand: String?) {
                // Check if context is not null before creating an instance of DataBaseHelper
                context?.let {
                    val dbHelper = DataBaseHelper(context)
                    val cars = dbHelper.getAllCars()
                    val filteredCars = if (brand == "All Brands") {
                        cars // Show all cars if "ALL BRANDS" is selected
                    } else {
                        cars.filter { it.brand == brand } // Filter cars by brand
                    }
                    adapter.updateCars(filteredCars)
                }
            }



        }


        val editUserButton: Button = findViewById(R.id.edit_booking_button)
        editUserButton.setOnClickListener {
            val intent = Intent(this, EditBookingActivity::class.java)
            startActivity(intent)
        }

        val navigateToUserEditButton: Button = findViewById(R.id.navigate_to_user_edit_button)
        navigateToUserEditButton.setOnClickListener {
            val intent = Intent(this, UserEdit::class.java)
            startActivity(intent)
        }


        val pickupDateButton = findViewById<Button>(R.id.pickup_date_button)
        pickupDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    calendar.set(year, month, dayOfMonth)
                    pickupDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                    pickupDateButton.text = "Pickup Date: $pickupDate"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }


        // Set up the extra1 CheckBox
        val extra1CheckBox = findViewById<CheckBox>(R.id.extra_1_check_box)
        extra1CheckBox.setOnCheckedChangeListener { _, isChecked ->
            extra1 = if (isChecked) "Extra 1" else null
        }

        // Set up the extra2 CheckBox
        val extra2CheckBox = findViewById<CheckBox>(R.id.extra_2_check_box)
        extra2CheckBox.setOnCheckedChangeListener { _, isChecked ->
            extra2 = if (isChecked) "Extra 2" else null
        }

        // Set up the duration Spinner
        val durationSpinner = findViewById<Spinner>(R.id.duration_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.duration_array,
            android.R.layout.simple_spinner_item
        ).also { adapterS ->
            adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            durationSpinner.adapter = adapterS
        }



        val bookButton = findViewById<Button>(R.id.book_button)
        bookButton.setOnClickListener {
            // Get the duration value from the Spinner as an Int
            val durationInt = durationSpinner.selectedItem.toString().replace("\\D+".toRegex(), "").toInt()

            // Calculate the return date based on the duration
            val calendar = Calendar.getInstance()
            calendar.time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(pickupDate)!!
            calendar.add(Calendar.DAY_OF_YEAR, durationInt)
            val returnDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

            // Assign the duration to the duration variable as a String
            duration = durationInt.toString()

            //  process the booking...



            selectedCar?.let {
                it.model?.let { it1 ->
                    val uniqueBookingId = generateUniqueBookingId(userId)
                    val isSuccess = dbHelper.addBooking(
                        uniqueBookingId,
                        userId,
                        it1,
                        pickupDate,
                        returnDate,
                        duration!!,
                        extra1,
                        extra2
                    )
                    if (isSuccess) {
                        // Start the BookingDetailsActivity and pass the booking details
                        val intent = Intent(this, BookingDetailsActivity::class.java).apply { }
                        intent.putExtra("bookingId", uniqueBookingId)
                        intent.putExtra("userId", userId)
                        intent.putExtra("model", it1)
                        intent.putExtra("pickupDate", pickupDate)
                        intent.putExtra("returnDate", returnDate)
                        intent.putExtra("duration", duration)
                        intent.putExtra("extra1", extra1)
                        intent.putExtra("extra2", extra2)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Failed to add booking", Toast.LENGTH_SHORT).show()
                    }


                }
            }
        }
    }
}


