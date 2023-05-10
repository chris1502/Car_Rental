package com.example.carrental.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.carrental.DataBaseHelper
import com.example.carrental.R

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        usernameEditText = findViewById(R.id.username_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)

        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Perform authentication logic here
            val isAdmin = checkIfAdmin(username, password)
            if (isAdmin) {
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Show error message
                Toast.makeText(this, "You must be an admin to log in", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkIfAdmin(username: String, password: String): Boolean {
        val db = DataBaseHelper(this)
        val user = db.getUser(username, password)
        return user?.isAdmin ?: false
    }
}
