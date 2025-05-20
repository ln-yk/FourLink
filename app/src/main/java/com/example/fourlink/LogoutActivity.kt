package com.example.fourlink

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fourlink.R.*

class LogoutActivity : TransitionActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.logout_screen)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN

        val button_log_out_confirm = findViewById<Button>(R.id.button_log_out_confirm)
        val button_log_out_cancel = findViewById<Button>(R.id.button_log_out_cancel)

        button_log_out_confirm.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            UserManager.logout()
            finish()
            startActivity(intent)
        }
        button_log_out_cancel.setOnClickListener{
            finish()
        }
    }
}