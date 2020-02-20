package com.example.sync

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.core.view.isVisible
import com.example.sync.Manager.IpPortManager
import com.example.sync.Manager.runSocket
import com.squareup.seismic.ShakeDetector
import org.json.JSONObject

class TrackpadModeActivity : AppCompatActivity(), ShakeDetector.Listener {

    private lateinit var menuButton: ImageButton
    private lateinit var settingButton: ImageButton
    private lateinit var finishedAcitivityButton: ImageButton
    private lateinit var ipPortManager: IpPortManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trackpad_mode)

        ipPortManager = IpPortManager(this)

        // UI初期化
        menuButton = findViewById(R.id.menu_button)
        settingButton = findViewById(R.id.setting_button)
        finishedAcitivityButton = findViewById(R.id.activity_finished_button)

        menuButton.tag = false
        menuButton.setOnClickListener {
            if (menuButton.tag as Boolean) {
                // hidden menu
                menuButton.setImageResource(R.drawable.home_ani_cross_to_menu)
                val drawable = menuButton.drawable as? AnimatedVectorDrawable
                if (drawable != null) {
                    if (!drawable.isRunning) {
                        drawable.start()
                    }
                } else {
                    (menuButton.drawable as Animatable).start()
                }

                settingButton.isVisible = false
                settingButton.startAnimation((AnimationUtils.loadAnimation(this, R.anim.main_buttons_hidden)))

                finishedAcitivityButton.isVisible = false
                finishedAcitivityButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_buttons_hidden_side))
            } else {
                // show menu
                menuButton.setImageResource(R.drawable.ani_menu_to_cross)
                val drawable = menuButton.drawable as? AnimatedVectorDrawable
                if (drawable != null) {
                    if (!drawable.isRunning) {
                        drawable.start()
                    }
                } else {
                    (menuButton.drawable as Animatable).start()
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
            val intent = Intent(this, SettingMenuActivity::class.java)
            startActivity(intent)
        }

        finishedAcitivityButton.isVisible = false
        finishedAcitivityButton.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        // リスナーをセット
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sd = ShakeDetector(this)
        sd.start(sensorManager)

        ipPortManager.reloadAddressInfo()

        // IPアドレスとポート番号が未設定の場合の初期設定
        val sharedPref = getSharedPreferences("ServerAddress", Context.MODE_PRIVATE)
        if (sharedPref.getString("ip", "") == "") {
            sharedPref.edit().putString("ip", "192.168.100.2").apply()
        }
        if (sharedPref.getInt("port", 0) == 0) {
            sharedPref.edit().putInt("port", 8080).apply()
        }
    }

    override fun hearShake() {

        val sendJson = JSONObject()
        sendJson.put("key", "shake")
        operateRequest(sendJson)
        Log.d("[shake]", "shake")
    }

    private fun operateRequest(send: JSONObject) {
        runSocket(ipPortManager.getIP(), ipPortManager.getPort(), send)
    }
}
