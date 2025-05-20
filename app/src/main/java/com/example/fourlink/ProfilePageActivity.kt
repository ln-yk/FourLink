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

class ProfilePageActivity : TransitionActivity() {
    var activeToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_screen)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        val buttonSave = findViewById<Button>(R.id.save_button)
        val buttonBack = findViewById<Button>(R.id.back_button)
        val logoutButton = findViewById<Button>(R.id.logout_button)
        val usernameText = findViewById<TextView>(R.id.et_name)
        val passwordInput = findViewById<EditText>(R.id.et_password)

        val currentUser = UserManager.currentUser

        if (currentUser == null) { // this is used when you are in the main menu or wherever and no user is logged in
            showToast("No user is logged in")
            // Toast.makeText(this, "No user is logged in", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        usernameText.text = currentUser.username
        passwordInput.setText(currentUser.password)

        logoutButton.setOnClickListener {
            startActivity(Intent(this, LogoutActivity::class.java))
        }

        buttonSave.setOnClickListener {
            val newPass = passwordInput.text.toString().trim()

            if (newPass.isEmpty()) {
                showToast("Password cannot be empty!")
                // Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPass.length < 6) {
                showToast("Password must be 6 or more characters!")
                // Toast.makeText(this, "Password must be 6 or more characters!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val currentUser = UserManager.currentUser
            if (currentUser == null) {
                showToast("No user logged in.")
                // Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = UserManager.updatePassword(currentUser.username, newPass)
            if (success) {
                UserManager.login(currentUser.username, newPass)
                showToast("Password updated successfully!")
                // Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show()
            } else {
                showToast("Failed to update password!")
                // Toast.makeText(this, "Failed to update password!", Toast.LENGTH_SHORT).show()
            }
        }


        buttonBack.setOnClickListener {
            finish()
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
