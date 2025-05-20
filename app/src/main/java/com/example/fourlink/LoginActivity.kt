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

class LoginActivity : TransitionActivity() {
    var activeToast: Toast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        val forgotPassword = findViewById<TextView>(R.id.forgot_password)
        val loginButton = findViewById<Button>(R.id.button_register)
        val usernameField = findViewById<EditText>(R.id.edit_user)
        val passwordField = findViewById<EditText>(R.id.edit_password)
        val registerButton = findViewById<TextView>(R.id.register_button)

        intent?.getStringExtra("username")?.let { usernameField.setText(it) }
        // intent?.getStringExtra("password")?.let { passwordField.setText(it) }

        loginButton.setOnClickListener {
            // startActivity(Intent(this, MainMenuActivity::class.java)) // for fun lang fo kay kapoy register
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                showToast("Username and Password must not be empty")
                // Toast.makeText(this, "Username and Password must not be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (UserManager.login(username, password)) {
                val intent = Intent(this, MainMenuActivity::class.java).apply {
                    putExtra("username", username)
                }
                finish()
                startActivity(intent)
            } else {
                showToast("Invalid Credentials")
                // Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_LONG).show()
            }
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        forgotPassword.setOnClickListener { // just create new account bro it's an offline game anyways
            showToast("The developers may or may not implement this functionality")
            // Toast.makeText(this, "The developers may or may not implement this functionality", Toast.LENGTH_LONG).show()
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
