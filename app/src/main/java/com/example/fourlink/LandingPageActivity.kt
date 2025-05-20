package com.example.fourlink

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LandingPageActivity : TransitionActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing_page_screen)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        val login_button = findViewById<Button>(R.id.login_button)
        val register_button = findViewById<Button>(R.id.register_button)

        login_button.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        register_button.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

    }
}