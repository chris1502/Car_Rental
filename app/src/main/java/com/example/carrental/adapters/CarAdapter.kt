package com.example.carrental.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.R

class CarAdapter(private var cars: List<Car>) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {
    private var onItemClickListener: ((Car?) -> Unit)? = null
    private var selectedPosition = -1

    fun setOnItemClickListener(listener: (Car?) -> Unit) {
        onItemClickListener = listener
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brand: TextView = itemView.findViewById(R.id.car_brand)
        val model: TextView = itemView.findViewById(R.id.car_model)
        val year: TextView = itemView.findViewById(R.id.car_year)
        val price: TextView = itemView.findViewById(R.id.car_price)
        val description: TextView = itemView.findViewById(R.id.car_description)
        val location: TextView = itemView.findViewById(R.id.car_location)
        val transmission: TextView = itemView.findViewById(R.id.car_transmission)
        val seats: TextView = itemView.findViewById(R.id.car_seats)


        val radioButton: RadioButton = itemView.findViewById(R.id.car_radio_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.car_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car = cars[position]
        holder.brand.text = car.brand
        holder.model.text = car.model
        holder.year.text = car.year.toString()
        holder.price.text = car.price.toString()
        holder.description.text = car.description
        holder.location.text = car.location
        holder.seats.text = car.seats.toString()
        holder.transmission.text = car.transmission


        holder.radioButton.isChecked = selectedPosition == position
        holder.radioButton.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            onItemClickListener?.invoke(getSelectedCar())
        }

        holder.itemView.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            onItemClickListener?.invoke(getSelectedCar())
        }
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    fun getSelectedCar(): Car? {
        return if (selectedPosition != -1) {
            cars[selectedPosition]
        } else {
            null
        }
    }
    fun updateCars(cars: List<Car>) {
        this.cars = cars
        notifyDataSetChanged()
    }

}
