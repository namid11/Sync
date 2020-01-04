package com.example.sync

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import com.squareup.seismic.ShakeDetector
import org.json.JSONObject
import kotlin.math.abs

class MainActivity : AppCompatActivity(), SensorEventListener, ShakeDetector.Listener {

    private lateinit var trackView: View
    private lateinit var demoView: View
    private lateinit var settingButton: ImageButton

    val accSensorManager = AccSensorManager(v_flag = false, buf_time = null)
    private lateinit var touchPointManager: TouchPointManager


    enum class REQUEST_INTENT {
        SETTING
    }


    // Creating View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        touchPointManager = TouchPointManager(this)

        trackView = findViewById(R.id.trackView)
        trackView.setOnTouchListener { v, event ->  touchProcess(v, event)}

        demoView = findViewById(R.id.demoView)

        settingButton = findViewById(R.id.settingButton)
        settingButton.setOnClickListener {
            val intent = Intent(this, IpPortSettingActivity::class.java)
            startActivityForResult(intent, REQUEST_INTENT.SETTING.ordinal)
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
            REQUEST_INTENT.SETTING.ordinal -> {
                if (resultCode == Activity.RESULT_OK) {
                    touchPointManager.ipPortManager.reloadAddressInfo()
                }
            }
        }
    }


    // シェイク検知
    override fun hearShake() {
        // やりたい処理を書く
        touchPointManager.shake()
    }



    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


    // Sensor
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    // 横の傾き検知。およびフラグが立っていない場合。
                    if (event.values[0] > 0.8 && !accSensorManager.v_flag) {

                    }
                }

                // 照度センサー
                Sensor.TYPE_LIGHT -> {
                    Log.d("Activity","Sensor.TYPE_LIGHT :" + event.values[0].toString())
                    val light_param = event.values[0]
                    if (light_param < 5f) {
                        touchPointManager.gloomy(true)
                    } else {
                        touchPointManager.gloomy(false)
                    }
                }
            }
        }
    }


    // View上のタッチ反応取得イベント
    private fun touchProcess(v: View, event: MotionEvent): Boolean {
//        Log.d("[Log] TouchProcess", "message - %s".format(MotionEvent.ACTION_DOWN == event.action).toString())
//        Log.d("[Log] TouchProcess", "x:%f, y:%f".format(event.x, event.y))
//        val animation = AnimationUtils.loadAnimation(this, R.anim.demo_ani)
//        demoView.startAnimation(animation)

        when (event.pointerCount) {
            // １本指
            1 -> {
                Log.d("[LOG] - DEBUG", "pointer count 1")
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
                        touchPointManager.up(TouchPoint(event.x, event.y), event.eventTime)
                    }
                }
            }

            // ２本指
            2 -> {
                Log.d("[LOG] - DEBUG", "pointer count 2")
                touchPointManager.twoTap = true
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        touchPointManager.first_down(TouchPoint(event.x, event.y), event.eventTime)
                        Log.d("[LOG] - DEBUG", "ACTION_DOWN")
                    }

                    MotionEvent.ACTION_MOVE -> {
                        touchPointManager.scroll(TouchPoint(event.x, event.y), event.eventTime)
                        Log.d("[LOG] - DEBUG", "ACTION_MOVE")
                    }

                    MotionEvent.ACTION_UP -> {
                        touchPointManager.double(event.eventTime)
                    }
                }
            }

            // 3本指
            3 -> {
                Log.d("[LOG] - DEBUG", "pointer count 3, action: %s".format(event.action.toString()))
                touchPointManager.tripleTap = true
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        touchPointManager.first_down(TouchPoint(event.x, event.y), event.eventTime)
                        Log.d("[LOG] - DEBUG", "ACTION_DOWN")
                    }

                    MotionEvent.ACTION_MOVE -> {
                    }

                    MotionEvent.ACTION_UP -> {
                        touchPointManager.triple(event.eventTime)
                    }



                }
            }
        }

        demoView.x = event.x
        demoView.y = event.y
        return true
    }



    data class AccSensorManager(var v_flag: Boolean, var buf_time: Long?)

    data class TouchPoint(var x:Float, var y:Float)

    class TouchPointManager(context: Context) {

        val ipPortManager = IpPortManager(context)

        // タップフラグ
        var down: Boolean = false
        var twoTap: Boolean = false
        var tripleTap: Boolean = false

        // タップ用変数
        var firstTouchPoint: TouchPoint? = null
        var firstTouchTime: Long = 0

        // 照度フラグ
        var lightOn: Boolean = false

        // スクロール用変数
        var scrollPoint: TouchPoint? = null
        var scrollTime: Long = 0

        var forceStop: Boolean = false  // 強制停止フラグ

        fun first_down(point: TouchPoint, time: Long) {
            if (forceStop) return

            down = true
            val postJsonObj = JSONObject()
            postJsonObj.put("key", "first")
            firstTouchPoint = point
            runSocket(ipPortManager.getIP(), ipPortManager.getPort(), postJsonObj)

            firstTouchTime = time
        }

        fun moved(point: TouchPoint) {
            if (forceStop) return

            if (!twoTap && !tripleTap) {
                if (down && firstTouchPoint != null) {
                    val postJsonObj = JSONObject()
                    postJsonObj.put("key", "moved")

                    postJsonObj.put("context", JSONObject().apply {
                        put("x", point.x - firstTouchPoint!!.x)
                        put("y", point.y - firstTouchPoint!!.y)
                    })

                    runSocket(ipPortManager.getIP(), ipPortManager.getPort(), postJsonObj)
                } else {
                    Log.d("[LOG] - ERROR", "don't find pointBefore")
                }
            }
        }

        fun up(point: TouchPoint, time: Long) {
            if (forceStop) {
                reset()
                return
            }

            if (tripleTap) {
                // 三本指タップ
                triple(time)
            } else if (twoTap) {
                // 二本指タップ
                val postJsonObj = JSONObject()
                postJsonObj.put("key", "two_fingers_click")
                if (time - firstTouchTime < 150) {
                    runSocket(ipPortManager.getIP(), ipPortManager.getPort(), postJsonObj)
                }
            } else {
                // １本指タップ
                val postJsonObj = JSONObject()
                postJsonObj.put("key", "click")
                if (time - firstTouchTime < 150) {
                    runSocket(ipPortManager.getIP(), ipPortManager.getPort(), postJsonObj)
                }
            }

            this.twoTap = false
            this.tripleTap = false
        }

        fun scroll(point: TouchPoint, time: Long) {
            if (forceStop || tripleTap) return

            if (scrollTime == 0L || scrollPoint == null) {
                scrollTime = time
                scrollPoint = point
                Log.d("[LOG]", "Set scroll param")
            } else {
                val dist_x = point.x - scrollPoint!!.x
                val dist_y = point.y - scrollPoint!!.y
                val d_time = time - scrollTime
                val vec_x = dist_x / (d_time)
                val vec_y = dist_y / (d_time)
                Log.d("[LOG]", "dist_x: %f, dist_y: %f, d_time: %d ,x: %f, y:%f".format(dist_x, dist_y, d_time.toInt(), vec_x, vec_y))

                if (abs(dist_x) > 300) {
                    // ブラウザバック
                    if (dist_x > 0) {
                        Log.d("TouchPointManager", "browser_back")
                        val postJsonObj = JSONObject()
                        postJsonObj.put("key", "browser_back")
                        runSocket(ipPortManager.getIP(), ipPortManager.getPort(), postJsonObj)
                        forceStop = true
                    } else {
                        Log.d("TouchPointManager", "browser_forward")
                        val postJsonObj = JSONObject()
                        postJsonObj.put("key", "browser_forward")
                        runSocket(ipPortManager.getIP(), ipPortManager.getPort(), postJsonObj)
                        forceStop = true
                    }

                } else if (abs(dist_x) > 20 || abs(dist_y) > 20) {
                    // 移動量が少ないとスクロールさせない
                    val postJsonObj = JSONObject()
                    postJsonObj.put("key", "scroll")
                    postJsonObj.put("context", JSONObject().apply {
                        put("x", ((point.x - firstTouchPoint!!.x)).toInt())
                        put("y", ((point.y - firstTouchPoint!!.y)).toInt())
                    })
                    runSocket(ipPortManager.getIP(), ipPortManager.getPort(), postJsonObj)
                }

                scrollTime = time
                scrollPoint = point
            }
        }

        fun double(time: Long) {
            if (forceStop) return

            val postJsonObj = JSONObject()
            postJsonObj.put("key", "two_fingers_click")
            if (time - firstTouchTime < 150) {
                runSocket(ipPortManager.getIP(), ipPortManager.getPort(), postJsonObj)
            }
        }

        fun triple(time: Long) {
            if (forceStop) return

            val postJsonObj = JSONObject()
            postJsonObj.put("key", "triple_fingers_click")
            if (time - firstTouchTime < 150) {
                runSocket(ipPortManager.getIP(), ipPortManager.getPort(), postJsonObj)
            }
        }

        fun shake() {
            if (forceStop) return

            val jsonObj = JSONObject()
            jsonObj.put("key", "shake")
            runSocket(ipPortManager.getIP(), ipPortManager.getPort(), jsonObj)
        }

        fun gloomy(hide: Boolean) {
            if (forceStop) return

            Log.d("TouchPointManager", "[gloomy()] called")

            if (hide && lightOn == false) {
                Log.d("TouchPointManager", "LockOn")
                val jsonObj = JSONObject()
                jsonObj.put("key", "lock")
                runSocket(ipPortManager.getIP(), ipPortManager.getPort(), jsonObj)
                lightOn = true
            } else if (hide == false && lightOn) {
                Log.d("TouchPointManager", "LockOff")
                lightOn = false
            }

        }

        fun reset() {
            down = false
            twoTap = false
            firstTouchPoint = null
            firstTouchTime = 0
            forceStop = false
        }
    }
}
