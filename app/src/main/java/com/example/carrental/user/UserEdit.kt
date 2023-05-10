package com.example.carrental.user

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.carrental.DataBaseHelper
import com.example.carrental.R

class UserEdit : AppCompatActivity() {

    private lateinit var dbHelper: DataBaseHelper
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit)

        // Get the current user's email and password from the Intent extras
        val email = intent.getStringExtra("email") ?: ""
        val password = intent.getStringExtra("password") ?: ""

        // Create an instance of the DataBaseHelper class
        dbHelper = DataBaseHelper(this)

        // Get the current user's information from the database
        val currentUser = dbHelper.getUser(email, password)

        // Find the EditText views by their IDs
        nameEditText = findViewById(R.id.name_edit_text)
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)

        // Display the current user's information in EditText fields for editing
        nameEditText.setText(currentUser?.name)
        emailEditText.setText(currentUser?.email)
        passwordEditText.setText(currentUser?.password)

        // When the user clicks the "Save" button, update their information in the database
        val saveButton = findViewById<Button>(R.id.save_button)

        saveButton.setOnClickListener {
            val newName = nameEditText.text.toString()
            val newEmail = emailEditText.text.toString()
            val newPassword = passwordEditText.text.toString()

            if (currentUser != null && dbHelper.updateUser(currentUser.id, newName, newEmail, newPassword)) {
                Toast.makeText(this, "User information updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error updating user information", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Close the database connection when the activity is destroyed
        dbHelper.close()
    }
}
