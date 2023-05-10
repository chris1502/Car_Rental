package com.example.carrental.user

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.carrental.DataBaseHelper
import com.example.carrental.R
import com.example.carrental.adapters.User
import com.google.android.material.snackbar.Snackbar

class CreateUserActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        nameEditText = findViewById(R.id.name_edit_text)
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)

        val createUserButton = findViewById<Button>(R.id.create_user_button)
        createUserButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val db = DataBaseHelper(this)
            val user = db.getUser(email, password)
            if (user == null) {
                val newUser = User(0, name, email, password, false) // false for regular user
                db.addUser(newUser)
                Snackbar.make(it, "User created", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(it, "User already exists", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
