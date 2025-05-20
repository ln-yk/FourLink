package com.example.fourlink

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class RegisterActivity : TransitionActivity() {
    var activeToast: Toast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_screen)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        val log_in_button = findViewById<TextView>(R.id.log_in_button)
        val register_button = findViewById<Button>(R.id.button_register)
        val username = findViewById<EditText>(R.id.edit_user)
        val password = findViewById<EditText>(R.id.edit_password)
        val confirm_password = findViewById<EditText>(R.id.edit_confirm_password)

        register_button.setOnClickListener {
            val usernameText = username.text.toString().trim()
            val passwordText = password.text.toString()
            val confirmPasswordText = confirm_password.text.toString()

            if (usernameText.isEmpty() || passwordText.isEmpty() || confirmPasswordText.isEmpty()) {
                showToast("Fields must not be empty!")
                // Toast.makeText(this, "Fields must not be empty!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (usernameText.length < 3) {
                showToast("Username must be 3 or more characters!")
                return@setOnClickListener
            }

            if (passwordText.length < 6) {
                showToast("Password must be 6 or more characters!")
                // Toast.makeText(this, "Password must be 6 or more characters!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (passwordText != confirmPasswordText) {
                showToast("Passwords do not match!")
                // Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val succ = UserManager.register(usernameText, passwordText)
            if (!succ) {
                showToast("Username already exists")
                // Toast.makeText(this, "Username already exists", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            showToast("Account successfully created!")
            // Toast.makeText(this, "Account successfully created!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginActivity::class.java).apply {
                putExtra("username", usernameText)
                putExtra("password", passwordText)
            })
        }

        log_in_button.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    fun showToast(message: String, duration: Long = 2000L) {
        activeToast?.cancel()
        activeToast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        activeToast?.show()

        Handler(Looper.getMainLooper()).postDelayed({
            activeToast?.cancel()
        }, duration)
    }
}
