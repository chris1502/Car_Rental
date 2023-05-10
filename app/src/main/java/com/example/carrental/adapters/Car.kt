package com.example.carrental.adapters

import android.os.Parcel
import android.os.Parcelable

data class Car(
    val id: String?,
    val brand: String? = null,
    val model: String? = null,
    val year: Int? = null,
    val colour: String? = null,
    val transmission: String? = null,
    val seats: String? = null,
    val price: Double? = null,
    val imageUrl: String? = null,
    val description: String? = null,
    val location: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(brand)
        parcel.writeString(model)
        if (year != null) {
            parcel.writeInt(year)
        }
        parcel.writeString(colour)
        parcel.writeString(transmission)
        parcel.writeString(seats)


        if (price != null) {
            parcel.writeDouble(price)
        }
        parcel.writeString(imageUrl)
        parcel.writeString(description)
        parcel.writeString(location)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Car> {
        override fun createFromParcel(parcel: Parcel): Car {
            return Car(parcel)
        }

        override fun newArray(size: Int): Array<Car?> {
            return arrayOfNulls(size)
        }
    }
}






