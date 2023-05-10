package com.example.carrental

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.carrental.adapters.Car
import com.example.carrental.booking.Booking
import com.example.carrental.adapters.User


class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "DataBase.db"

        // Users Table
        const val TABLE_USERS = "Users"
        const val COL_ID = "Id"
        const val COL_NAME = "Name"
        private const val COL_EMAIL = "Email"
        private const val COL_PASSWORD = "Password"
        private const val COL_IS_ADMIN = "is_admin"


        // Cars Table
        const val TABLE_CARS = "cars"
        const val COL_IDs = "ids"
        const val COL_MODEL = "model"
        const val COL_LOCATION = "location"
        const val COL_DESCRIPTION = "description"
        const val COL_PRICE = "price"
        const val COL_YEAR = "year"
        const val COL_TRANSMISSION = "transmission"
        const val COL_SEATS = "seats"
        const val  COL_BRAND ="brand"

        // Bookings Table
        private const val TABLE_BOOKINGS = "Bookings"
        private const val COL_BOOKING_ID = "booking_id"
        private const val COL_USER_ID = "UserId"
        private const val COL_PICKUP_DATE = "PickupDate"
        private const val COL_RETURN_DATE = "ReturnDate"
        private const val COL_DURATION = "duration"
        private const val COL_INSURANCE = "Insurance"
        private const val COL_GPS = "GPS"
        const val COL_CAR_MODEL = "model"


    }

    override fun onCreate(db: SQLiteDatabase) {

        val createTableQuery =
            "CREATE TABLE IF NOT EXISTS $TABLE_USERS ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NAME TEXT, $COL_EMAIL TEXT, $COL_PASSWORD TEXT, $COL_IS_ADMIN INTEGER)"

        val createBookingsTable =
            "CREATE TABLE $TABLE_BOOKINGS ($COL_BOOKING_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_USER_ID INTEGER, $COL_MODEL TEXT, $COL_PICKUP_DATE TEXT, $COL_RETURN_DATE TEXT, $COL_DURATION INTEGER, $COL_INSURANCE TEXT, $COL_GPS TEXT)"
        db.execSQL("CREATE TABLE Locations (location TEXT PRIMARY KEY)")
        val createCarsTable =
            "CREATE TABLE IF NOT EXISTS $TABLE_CARS ($COL_BRAND TEXT,$COL_MODEL TEXT ,$COL_TRANSMISSION TEXT,$COL_SEATS TEXT,$COL_YEAR INTEGER, $COL_LOCATION TEXT, $COL_DESCRIPTION TEXT, $COL_PRICE INTEGER, $COL_IDs INTEGER  PRIMARY KEY AUTOINCREMENT )"



        db.execSQL(createCarsTable)
        db.execSQL(createBookingsTable)
        db.execSQL(createTableQuery)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Cars")
        db?.execSQL("DROP TABLE IF EXISTS Locations")
        db?.execSQL("DROP TABLE IF EXISTS Bookings")
        if (db != null) {
            onCreate(db)
        }
    }



    fun getUserId(userName: String): Int {
        val db = readableDatabase
        val query = "SELECT $COL_ID FROM $TABLE_USERS WHERE $COL_NAME = ?"
        val cursor = db.rawQuery(query, arrayOf(userName))
        val userId = if (cursor.moveToFirst()) {
            cursor.getInt(0)
        } else {
            -1
        }
        cursor.close()
        return userId
    }






    fun addCar(model: String, location: String, description: String, price: Int, year: Int, Ids: String, transmission: String, seats: String, brand: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_MODEL, model)
            put(COL_LOCATION, location)
            put(COL_DESCRIPTION, description)
            put(COL_PRICE, price)
            put(COL_IDs,Ids)
            put(COL_YEAR, year)
            put(COL_TRANSMISSION,transmission)
            put(COL_SEATS, seats)
            put(COL_BRAND, brand)
        }
        val id = db.insert(TABLE_CARS, null, values).toInt()
        db.close()
        return id
    }

    fun removeCar(ids: Int) {
        val db = writableDatabase
        db.delete(TABLE_CARS, "$COL_IDs=?", arrayOf(arrayOf(ids).toString()))
        db.close()

    }
    fun removeUser(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_USERS, "$COL_ID = ?", arrayOf(id.toString()))
    }


    fun removeBookingsByUser(userName: String): Int {
        val db = writableDatabase
        val whereClause =
            "$TABLE_BOOKINGS.$COL_USER_ID = (SELECT $COL_ID FROM $TABLE_USERS WHERE $COL_NAME = ?)"
        val whereArgs = arrayOf(userName)
        val rowsDeleted = db.delete(TABLE_BOOKINGS, whereClause, whereArgs)
        db.close()
        return rowsDeleted
    }


    fun getAllCars(): List<Car> {
        val cars = mutableListOf<Car>()
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM Cars", null)

        if (cursor.moveToFirst()) {
            do {
                val ids = cursor.getString(cursor.getColumnIndexOrThrow("ids"))
                val brand = cursor.getString(cursor.getColumnIndexOrThrow("brand"))
                val model = cursor.getString(cursor.getColumnIndexOrThrow("model"))
                val year = cursor.getInt(cursor.getColumnIndexOrThrow("year"))
                val colour = cursor.getString(cursor.getColumnIndexOrThrow("colour"))
                val transmission = cursor.getString(cursor.getColumnIndexOrThrow("transmission"))
                val seats = cursor.getString(cursor.getColumnIndexOrThrow("seats"))
                val pricePerDay = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val location = cursor.getString(cursor.getColumnIndexOrThrow("location"))

                val car = Car(
                    ids,
                    brand,
                    model,
                    year,
                    colour,
                    transmission,
                    seats,
                    pricePerDay,
                    null,
                    description,
                    location
                )

                cars.add(car)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return cars
    }


    fun addBooking(
        bookingId: String,
        userId: Int,
        model: String,
        pickupDate: String,
        returnDate: String,
        duration: String,
        extra1: String?,
        extra2: String?
    ): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_USER_ID, userId)
            put(COL_CAR_MODEL, model)
            put(COL_PICKUP_DATE, pickupDate)
            put(COL_RETURN_DATE, returnDate)
            put(COL_DURATION, duration)
            put(COL_INSURANCE, extra1)
            put(COL_GPS, extra2)
        }

        val newRowId = db.insert(TABLE_BOOKINGS, null, values)
        return newRowId != -1L


    }


    fun addUser(user: User): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(COL_NAME, user.name)
        contentValues.put(COL_EMAIL, user.email)
        contentValues.put(COL_PASSWORD, user.password)
        contentValues.put(COL_IS_ADMIN, if (user.isAdmin) 1 else 0)

        val id = db.insert(TABLE_USERS, null, contentValues)
        db.close()

        return id
    }


    fun getUser(email: String, password: String): User? {
        val db = this.readableDatabase
        val query =
            "SELECT $COL_ID, $COL_NAME, $COL_EMAIL, $COL_PASSWORD, $COL_IS_ADMIN FROM $TABLE_USERS WHERE $COL_EMAIL = ? AND $COL_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        var user: User? = null

        if (cursor.moveToFirst()) {
            val idIndex = cursor.getColumnIndex(COL_ID)
            val nameIndex = cursor.getColumnIndex(COL_NAME)
            val emailIndex = cursor.getColumnIndex(COL_EMAIL)
            val passwordIndex = cursor.getColumnIndex(COL_PASSWORD)
            val isAdminIndex = cursor.getColumnIndex(COL_IS_ADMIN)
            if (idIndex >= 0 && nameIndex >= 0 && emailIndex >= 0 && passwordIndex >= 0 && isAdminIndex >= 0) {
                val id = cursor.getInt(idIndex)
                val name = cursor.getString(nameIndex)
                val email = cursor.getString(emailIndex)
                val password = cursor.getString(passwordIndex)
                val isAdmin = cursor.getInt(isAdminIndex) == 1
                user = User(id, name, email, password, isAdmin)
            }
        }
        cursor.close()
        return user
    }


    fun getUserBookingById(bookingId: Int): Booking? {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_BOOKINGS WHERE $COL_BOOKING_ID = ?",
            arrayOf(bookingId.toString())
        )

        fun getUserId(userName: String): Int {
            val db = readableDatabase
            val query = "SELECT $COL_ID FROM $TABLE_USERS WHERE $COL_NAME = ?"
            val cursor = db.rawQuery(query, arrayOf(userName))
            val userId = if (cursor.moveToFirst()) {
                cursor.getInt(0)
            } else {
                -1
            }
            cursor.close()
            return userId
        }


        return if (cursor.moveToFirst()) {
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID))
            val model = cursor.getString(cursor.getColumnIndexOrThrow(COL_CAR_MODEL))
            val pickupDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_PICKUP_DATE))
            val returnDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_RETURN_DATE))
            val duration = cursor.getString(cursor.getColumnIndexOrThrow(COL_DURATION))
            val extra1 = cursor.getString(cursor.getColumnIndexOrThrow(COL_INSURANCE))
            val extra2 = cursor.getString(cursor.getColumnIndexOrThrow(COL_GPS))

            Booking(bookingId, userId, model, pickupDate, returnDate, duration, extra1, extra2)
        } else {
            null
        }
    }

    fun updateBooking(booking: Booking): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_USER_ID, booking.userId)
            put(COL_CAR_MODEL, booking.model)
            put(COL_PICKUP_DATE, booking.pickupDate)
            put(COL_RETURN_DATE, booking.returnDate)
            put(COL_DURATION, booking.duration)
        }

        val numberOfRowsAffected = db.update(
            TABLE_BOOKINGS,
            contentValues,
            "$COL_BOOKING_ID = ?",
            arrayOf(booking.booking_id.toString())
        )

        db.close()
        return numberOfRowsAffected > 0
    }

    fun updateUser(id: Int, name: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, name)
            put(COL_EMAIL, email)
            put(COL_PASSWORD, password)
        }
        val result = db.update(TABLE_USERS, values, "$COL_ID=?", arrayOf(id.toString()))
        db.close()
        return result != -1
    }

}





