package com.example.sync

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sync.Manager.*

class HomeActivity : AppCompatActivity() {

    private lateinit var trackpadConstraintView: ConstraintLayout
    private lateinit var presentationConstraintView: ConstraintLayout
    private lateinit var settingButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        trackpadConstraintView = findViewById(R.id.trackpad_constraintView)
        presentationConstraintView = findViewById(R.id.presentation_constrainView)

        trackpadConstraintView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mode", MODE.TRACKPAD)
            startActivity(intent)
        }

        presentationConstraintView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mode", MODE.PRESENTATION)
            startActivity(intent)
        }

        settingButton = findViewById(R.id.home_settingButton)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingMenuActivity::class.java)
            startActivity(intent)
        }

    }
}
