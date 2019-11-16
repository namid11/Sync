package com.example.sync

import android.content.Context
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.Touch
import android.util.JsonReader
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import com.squareup.seismic.ShakeDetector
import org.json.JSONObject
import java.io.*
import java.net.*

class MainActivity : AppCompatActivity(), SensorEventListener, ShakeDetector.Listener {

    private lateinit var trackView: View
    private lateinit var demoView: View
    private lateinit var settingButton: ImageButton

    val accSensorManager = AccSensorManager(v_flag = false, buf_time = null)

    val touchPointManager = TouchPointManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trackView = findViewById(R.id.trackView)
        trackView.setOnTouchListener { v, event ->  touchProcess(v, event)}

        demoView = findViewById(R.id.demoView)

        settingButton = findViewById(R.id.settingButton)
        settingButton.setOnClickListener {

        }
    }

    override fun onResume() {
        super.onResume()


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
//                Log.d("[LOG] - DEBUG", "x: %f , y: %f, z: %f".format(event.values[0], event.values[1], event.values[2]))

                // 横の傾き検知。およびフラグが立っていない場合。
                if (event.values[0] > 0.8 && !accSensorManager.v_flag) {

                }
            }
        }
    }

    // View上のタッチ反応取得イベント
    private fun touchProcess(v: View, event: MotionEvent): Boolean {
//        Log.d("[Log] TouchProcess", "message - %s".format(MotionEvent.ACTION_DOWN == event.action).toString())
        Log.d("[Log] TouchProcess", "x:%f, y:%f".format(event.x, event.y))
//        val animation = AnimationUtils.loadAnimation(this, R.anim.demo_ani)
//        demoView.startAnimation(animation)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchPointManager.first_down(TouchPoint(event.x, event.y), event.eventTime)
                Log.d("[LOG] - DEBUG", "ACTION_DOWN")
            }

            MotionEvent.ACTION_MOVE -> {
                touchPointManager.moved(TouchPoint(event.x, event.y))
                Log.d("[LOG] - DEBUG", "ACTION_MOVE")
            }

            MotionEvent.ACTION_UP -> {
                Log.d("[LOG] - DEBUG", "%s".format(touchPointManager.up(TouchPoint(event.x, event.y), event.eventTime)))
            }


//            MotionEvent.ACTION_CANCEL -> {
//                Log.d("[LOG] - DEBUG", "ACTION_CANCEL")
//            }
        }

        demoView.x = event.x
        demoView.y = event.y
        return true
    }



    data class AccSensorManager(var v_flag: Boolean, var buf_time: Long?)

    data class TouchPoint(var x:Float, var y:Float)

    class TouchPointManager {

        var down: Boolean = false
        var firstTouchPoint: TouchPoint? = null
        var firstTouchTime: Long = 0

        fun first_down(point: TouchPoint, time: Long) {
            down = true
            val postJsonObj = JSONObject()
            postJsonObj.put("key", "first")
//            postJsonObj.put("context", JSONObject().apply {
//                put("x", point.x)
//                put("y", point.y)
//            })
            firstTouchPoint = point
            runSocket(postJsonObj)

            firstTouchTime = time
        }

        fun moved(point: TouchPoint) {
            if (down && firstTouchPoint != null) {
                val postJsonObj = JSONObject()
                postJsonObj.put("key", "moved")

                postJsonObj.put("context", JSONObject().apply {
                    put("x", point.x - firstTouchPoint.x)
                    put("y", point.y - firstTouchPoint.y)
                })

                runSocket(postJsonObj)
            } else {
                Log.d("[LOG] - ERROR", "don't find pointBefore")
            }
        }

        fun up(point: TouchPoint, time: Long): Boolean {
            val postJsonObj = JSONObject()
            postJsonObj.put("key", "click")
            runSocket(postJsonObj)
            return time - firstTouchTime < 300
        }
    }
}
