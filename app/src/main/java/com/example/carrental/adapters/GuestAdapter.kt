package com.example.carrental.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.LoginActivity
import com.example.carrental.R

class GuestAdapter(private val cars: List<Car>) :
    RecyclerView.Adapter<GuestAdapter.GuestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.car_item_guest, parent, false)
        return GuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        val car = cars[position]
        holder.bind(car)
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    class GuestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val modelTextView: TextView = itemView.findViewById(R.id.model_text_view)
        private val descriptionTextView: TextView =
            itemView.findViewById(R.id.description_text_view)
        private val priceTextView: TextView = itemView.findViewById(R.id.price_text_view)
        private val bookNowButton: Button = itemView.findViewById(R.id.book_now_button)

        init {
            bookNowButton.setOnClickListener {
                val context: Context = itemView.context
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }
        }

        fun bind(car: Car) {
            modelTextView.text = car.model
            descriptionTextView.text = car.description
            priceTextView.text = "$" + car.price.toString()
        }
    }
}


