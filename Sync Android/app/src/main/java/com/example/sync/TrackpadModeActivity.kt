package com.example.sync

import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.core.view.isVisible

class TrackpadModeActivity : AppCompatActivity() {

    private lateinit var menuButton: ImageButton
    private lateinit var settingButton: ImageButton
    private lateinit var finishedAcitivityButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trackpad_mode)

        // UI初期化
        menuButton = findViewById(R.id.menu_button)
        settingButton = findViewById(R.id.setting_button)
        finishedAcitivityButton = findViewById(R.id.activity_finished_button)

        menuButton.tag = false
        menuButton.setOnClickListener {
            if (menuButton.tag as Boolean) {
                // hidden menu
                menuButton.setImageResource(R.drawable.home_ani_cross_to_menu)
                val drawable = menuButton.drawable as AnimatedVectorDrawable
                if (!drawable.isRunning) {
                    drawable.start()
                }

                settingButton.isVisible = false
                settingButton.startAnimation((AnimationUtils.loadAnimation(this, R.anim.main_buttons_hidden)))

                finishedAcitivityButton.isVisible = false
                finishedAcitivityButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_hidden_side))
            } else {
                // show menu
                menuButton.setImageResource(R.drawable.ani_menu_to_cross)
                val drawable = menuButton.drawable as AnimatedVectorDrawable
                if (!drawable.isRunning) {
                    drawable.start()
                }

                settingButton.isVisible = true
                settingButton.startAnimation((AnimationUtils.loadAnimation(this, R.anim.main_buttons_show)))

                finishedAcitivityButton.isVisible = true
                finishedAcitivityButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_show_side))
            }

            // change flag
            menuButton.tag = !(menuButton.tag as Boolean)
        }

        settingButton.isVisible = false
        settingButton.setOnClickListener {

        }

        finishedAcitivityButton.isVisible = false
        finishedAcitivityButton.setOnClickListener {
            finish()
        }
    }


}
