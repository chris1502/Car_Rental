package com.example.carrental

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.carrental.admin.AdminLoginActivity
import com.example.carrental.admin.CreateAdminActivity
import com.example.carrental.booking.BookingActivity
import com.example.carrental.user.CreateUserActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)

        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val db = DataBaseHelper(this)
            val user = db.getUser(email,password, )
            if (user != null && user.password == password) {
                // login successful
                val intent = Intent(this, BookingActivity::class.java)
                intent.putExtra("userId", user.id)
                startActivity(intent)
            } else {
                // login failed
                Snackbar.make(it, "Invalid email or password", Snackbar.LENGTH_SHORT).show()
            }
        }

        val createUserButton = findViewById<Button>(R.id.create_user_button)
        createUserButton.setOnClickListener {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }

        val createAdminButton = findViewById<Button>(R.id.create_admin_button)
        createAdminButton.setOnClickListener {
            val intent = Intent(this, CreateAdminActivity::class.java)
            startActivity(intent)
        }

        val adminLoginButton = findViewById<Button>(R.id.admin_login_button)
        adminLoginButton.setOnClickListener {
            val intent = Intent(this, AdminLoginActivity::class.java)
            startActivity(intent)
        }
    }
}
