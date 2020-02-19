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
import com.example.sync.Manager.IpPortManager
import com.example.sync.Manager.runSocket
import com.squareup.seismic.ShakeDetector
import org.json.JSONObject


class PresentationModeActivity : AppCompatActivity(), SensorEventListener, ShakeDetector.Listener {

    // UI
    private lateinit var menuButton: ImageButton
    private lateinit var forwardButton: ImageButton
    private lateinit var backwardButton: ImageButton
    private lateinit var laserButton: ImageButton
    private lateinit var finishedActivityButton: ImageButton

    private lateinit var ipPortManager: IpPortManager

    // Creating View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presentation_mode)

        ipPortManager = IpPortManager(this)

        // ----- UI初期化 -----
        menuButton = findViewById(R.id.main_menuButton)
        menuButton.tag = false
        forwardButton = findViewById(R.id.forward_button)
        backwardButton = findViewById(R.id.backward_button)
        laserButton = findViewById(R.id.laser_button)
        finishedActivityButton = findViewById(R.id.activity_finished_button)

        val drawerGroup: List<ImageButton> = listOf(forwardButton, backwardButton, laserButton)

        menuButton.setOnClickListener {
            if (menuButton.tag as Boolean) {
                // hidden menu
                menuButton.setImageResource(R.drawable.home_ani_cross_to_menu)
                val drawable = menuButton.drawable as AnimatedVectorDrawable
                if (!drawable.isRunning) {
                    drawable.start()
                }

                for (view in drawerGroup) {
                    view.isVisible = false
                    view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_hidden))
                }

                finishedActivityButton.isVisible = false
                finishedActivityButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_hidden_side))
            } else {
                // show menu
                menuButton.setImageResource(R.drawable.ani_menu_to_cross)
                val drawable = menuButton.drawable as AnimatedVectorDrawable
                if (!drawable.isRunning) {
                    drawable.start()
                }

                for (view in drawerGroup) {
                    view.isVisible = true
                    view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_show))
                }

                finishedActivityButton.isVisible = true
                finishedActivityButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_show_side))
            }

            // change flag
            menuButton.tag = !(menuButton.tag as Boolean)

        }


        forwardButton.isVisible = false
        forwardButton.setOnClickListener {
            val sendJson = JSONObject()
            sendJson.put("key", "pp_next")
            operateRequest(sendJson)
        }

        backwardButton.isVisible = false
        backwardButton.setOnClickListener {
            val sendJson = JSONObject()
            sendJson.put("key", "pp_back")
            operateRequest(sendJson)
        }

        laserButton.isVisible = false
        laserButton.setOnClickListener {
            val sendJson = JSONObject()
            sendJson.put("key", "pp_laser")
            operateRequest(sendJson)
        }

        finishedActivityButton.isVisible = false
        finishedActivityButton.setOnClickListener {
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

        ipPortManager.reloadAddressInfo()
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
        val sendJson = JSONObject()
        sendJson.put("key", "shake")
        operateRequest(sendJson)
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

    private fun operateRequest(send: JSONObject) {
        runSocket(ipPortManager.getIP(), ipPortManager.getPort(), send)
    }
}
