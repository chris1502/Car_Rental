package com.example.carrental

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carrental.adapters.GuestAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = DataBaseHelper(this)
        val cars = dbHelper.getAllCars()

        val adapter = GuestAdapter(cars)
        recyclerView.adapter = adapter

        val loginButton = findViewById<Button>(R.id.loginbutton)
        loginButton.setOnClickListener {
            goToLoginActivity()
        }


    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}






