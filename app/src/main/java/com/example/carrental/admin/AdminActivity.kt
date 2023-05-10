package com.example.carrental.admin

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.carrental.DataBaseHelper
import com.example.carrental.R
import com.example.carrental.adapters.Car
import kotlin.random.Random

class AdminActivity : AppCompatActivity() {

    private lateinit var carSpinner: Spinner
    private lateinit var locationSpinner: Spinner
    private lateinit var userSpinner: Spinner
    private lateinit var carModelEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var yearEditText: EditText
    private lateinit var seatsSpinner: Spinner
    private lateinit var transmissionSpinner: Spinner
    private lateinit var brandEditText: EditText
    private var Ids: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        carSpinner = findViewById(R.id.car_spinner)
        locationSpinner = findViewById(R.id.location_spinner)
        userSpinner = findViewById(R.id.user_spinner)
        carModelEditText = findViewById(R.id.car_model_edit_text)
        descriptionEditText = findViewById(R.id.description_edit_text)
        priceEditText = findViewById(R.id.price_edit_text)
        yearEditText = findViewById(R.id.price_edit_year)
        seatsSpinner = findViewById(R.id.seats)
        transmissionSpinner = findViewById(R.id.transmission)
        brandEditText = findViewById(R.id.editBrand)


        fun generateUniqueIds(Ids: Int): String {
            val timestamp = System.currentTimeMillis()
            val randomInt = Random.nextInt(1, 9999)
            return "$Ids$timestamp$randomInt"
        }



        val db = DataBaseHelper(this)
        val cars = db.getAllCars() // Get a list of all available cars from the database
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cars)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        carSpinner.adapter = adapter


        locationSpinner = findViewById(R.id.location_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.location_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            locationSpinner.adapter = adapter
        }

        seatsSpinner = findViewById(R.id.seats)
        ArrayAdapter.createFromResource(
            this,
            R.array.seats_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            seatsSpinner.adapter = adapter
        }


        transmissionSpinner = findViewById(R.id.transmission)
        ArrayAdapter.createFromResource(
            this,
            R.array.transmission_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            transmissionSpinner.adapter = adapter
        }


        userSpinner = findViewById(R.id.user_spinner)

        carModelEditText = findViewById(R.id.car_model_edit_text)
        descriptionEditText = findViewById(R.id.description_edit_text)
        priceEditText = findViewById(R.id.price_edit_text)
        brandEditText = findViewById(R.id.editBrand)

        val addCarButton = findViewById<Button>(R.id.add_car_button)
        addCarButton.setOnClickListener {
            val model = carModelEditText.text?.toString()
            val seats = seatsSpinner.selectedItem.toString() // Get selected seats from spinner
            val location = locationSpinner.selectedItem.toString()
            val transmission = transmissionSpinner.selectedItem.toString()

            val description = descriptionEditText.text?.toString()
            val price = priceEditText.text?.toString()?.toIntOrNull()
            val year = yearEditText.text?.toString()?.toIntOrNull()
            val brand =  brandEditText.text.toString()?.toString()
            val Ids = generateUniqueIds(Ids)




            if (model != null && location.isNotEmpty() && description != null && price != null) {
                val db = DataBaseHelper(this)
                if (year != null) {
                    if (brand != null) {
                        db.addCar(model, location, description, price, year, Ids, seats, transmission, brand)
                    }
                }

                updateCarList()
            } else {
                // Show an error message or handle the error case here
            }
        }

        val removeCarButton = findViewById<Button>(R.id.remove_car_button)
        removeCarButton.setOnClickListener {
            val selectedCar = carSpinner.selectedItem as? String
            if (selectedCar != null) {
                val carId = selectedCar.split("|")[0].trim()
                    .toInt() // extract car ID from the selected string
                val db = DataBaseHelper(this)
                db.removeCar(carId)

                updateCarList()
            } else {
                // handle error case, e.g. show a message to the user
            }
        }

        val removeUserButton = findViewById<Button>(R.id.remove_user_button)
        removeUserButton.setOnClickListener {
            val selectedItem = userSpinner.selectedItem as String
            val db = DataBaseHelper(this)
            val userId = db.getUserId(selectedItem)
            db.removeBookingsByUser(selectedItem)
            db.removeUser(userId)

            updateUserList()
        }


        updateCarList()
        updateUserList()
    }

    private fun updateUserList() {
        val db = DataBaseHelper(this)
        val users = db.readableDatabase.rawQuery(
            "SELECT ${DataBaseHelper.COL_NAME} FROM ${DataBaseHelper.TABLE_USERS}",
            null
        )
        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_spinner_item)

        while (users.moveToNext()) {
            val nameIndex = users.getColumnIndex(DataBaseHelper.COL_NAME)
            if (nameIndex >= 0) {
                adapter.add(users.getString(nameIndex))
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userSpinner.adapter = adapter
        users.close()
    }

    private fun updateCarList() {
        val db = DataBaseHelper(this)
        val cars = db.getAllCars()
        val adapter: ArrayAdapter<Car> = ArrayAdapter(this, android.R.layout.simple_spinner_item, cars)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        carSpinner.adapter = adapter
    }
}
