package com.example.sync

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.core.view.isVisible
import com.squareup.seismic.ShakeDetector


class PresentationModeActivity : AppCompatActivity(), SensorEventListener, ShakeDetector.Listener {

    // UI
    private lateinit var menuButton: ImageButton
    private lateinit var forwardButton: ImageButton
    private lateinit var backwardButton: ImageButton
    private lateinit var presentationButton: ImageButton
    private lateinit var finishedAcitivityButton: ImageButton

    // Creating View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presentation_mode)

        // ----- UI初期化 -----
        menuButton = findViewById(R.id.main_menuButton)
        menuButton.tag = false
        forwardButton = findViewById(R.id.forward_button)
        backwardButton = findViewById(R.id.backward_button)
        presentationButton = findViewById(R.id.presen_button)
        finishedAcitivityButton = findViewById(R.id.activity_finished_button)

        menuButton.setOnClickListener {
            if (menuButton.tag as Boolean) {
                // hidden menu
                menuButton.setImageResource(R.drawable.home_ani_cross_to_menu)
                val drawable = menuButton.drawable as AnimatedVectorDrawable
                if (!drawable.isRunning) {
                    drawable.start()
                }

                forwardButton.isVisible = false
                forwardButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_hidden))

                backwardButton.isVisible = false
                backwardButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_hidden))

                presentationButton.isVisible = false
                presentationButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_hidden))

                finishedAcitivityButton.isVisible = false
                finishedAcitivityButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_hidden_side))
            } else {
                // show menu
                menuButton.setImageResource(R.drawable.ani_menu_to_cross)
                val drawable = menuButton.drawable as AnimatedVectorDrawable
                if (!drawable.isRunning) {
                    drawable.start()
                }

                forwardButton.isVisible = true
                forwardButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_show))

                backwardButton.isVisible = true
                backwardButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_show))

                presentationButton.isVisible = true
                presentationButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_show))

                finishedAcitivityButton.isVisible = true
                finishedAcitivityButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_show_side))
            }

            // change flag
            menuButton.tag = !(menuButton.tag as Boolean)

        }


        forwardButton.isVisible = false
        forwardButton.setOnClickListener {

        }

        backwardButton.isVisible = false
        backwardButton.setOnClickListener {

        }

        presentationButton.isVisible = false
        presentationButton.setOnClickListener {
        }

        finishedAcitivityButton.isVisible = false
        finishedAcitivityButton.setOnClickListener {
            finish()
        }
    }


    // Back to View
    override fun onResume() {
        super.onResume()

        // リスナーをセット
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sd = ShakeDetector(this)
        sd.start(sensorManager)

        // 加速度センサー取得
        val accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL)
        // 照度センサー取得
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)


        // IPアドレスとポート番号が未設定の場合の初期設定
        val sharedPref = getSharedPreferences("ServerAddress", Context.MODE_PRIVATE)
        if (sharedPref.getString("ip", "") == "") {
            sharedPref.edit().putString("ip", "192.168.100.2").apply()
        }
        if (sharedPref.getInt("port", 0) == 0) {
            sharedPref.edit().putInt("port", 8080).apply()
        }
    }


    // Leave this View
    override fun onPause() {
        super.onPause()

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)
    }


    // Get result any activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
        }
    }


    // シェイク検知
    override fun hearShake() {
        // やりたい処理を書く
    }



    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


    // Sensor
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {

                }

                // 照度センサー
                Sensor.TYPE_LIGHT -> {
//                    Log.d("Activity","Sensor.TYPE_LIGHT :" + event.values[0].toString())
                    val lightParam = event.values[0]
                    if (lightParam < 5f) {

                    } else {

                    }
                }
            }
        }
    }
}
