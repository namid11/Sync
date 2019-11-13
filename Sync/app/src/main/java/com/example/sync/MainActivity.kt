package com.example.sync

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import com.squareup.seismic.ShakeDetector
import org.json.JSONObject
import java.io.*
import java.net.*

class MainActivity : AppCompatActivity(), SensorEventListener, ShakeDetector.Listener {

    private lateinit var trackView: View
    private lateinit var demoView: View
    private lateinit var socketButton: Button

    val accSensorManager = AccSensorManager(v_flag = false, buf_time = null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trackView = findViewById(R.id.trackView)
        trackView.setOnTouchListener { v, event ->  touchProcess(v, event)}

        demoView = findViewById(R.id.demoView)
        socketButton = findViewById(R.id.socketButton)
    }

    override fun onResume() {
        super.onResume()

        socketButton.setOnClickListener {
            val jsonObj = JSONObject()
            jsonObj.put("key", "moved")
            val jsonContextObj = JSONObject()
            jsonContextObj.put("x", 10)
            jsonContextObj.put("y", 10)
            jsonObj.put("context", jsonContextObj)

            runSocket(jsonObj)
        }

        // リスナーをセット
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sd = ShakeDetector(this)
        sd.start(sensorManager)

        val accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)
    }

    // シェイク検知
    override fun hearShake() {
        // やりたい処理を書く
        Log.d("[LOG] - DEBUG", "get shake !")
        val jsonObj = JSONObject()
        jsonObj.put("key", "shake")
        runSocket(jsonObj)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                Log.d("[LOG] - DEBUG", "x: %f , y: %f, z: %f".format(event.values[0], event.values[1], event.values[2]))

                // 横の傾き検知。およびフラグが立っていない場合。
                if (event.values[0] > 0.8 && !accSensorManager.v_flag) {

                }
            }
        }
    }

    private fun touchProcess(v: View, event: MotionEvent): Boolean {
        Log.d("[Log] TouchProcess", "x:%f, y:%f".format(event.x, event.y))
//        val animation = AnimationUtils.loadAnimation(this, R.anim.demo_ani)
//        demoView.startAnimation(animation)
        demoView.x = event.x
        demoView.y = event.y
        return true
    }

    data class AccSensorManager(var v_flag: Boolean, var buf_time: Long?)
}
