package com.example.sync

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
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.core.view.isVisible
import com.example.sync.Manager.*
import com.squareup.seismic.ShakeDetector
import org.json.JSONObject

class MainActivity : AppCompatActivity(), SensorEventListener, ShakeDetector.Listener {

    // UI
    private lateinit var trackView: View
    private lateinit var demoView: View
    private lateinit var settingButton: ImageButton
    private lateinit var forwardButton: ImageButton
    private lateinit var backwardButton: ImageButton
    private lateinit var presenButton: ImageButton

    val accSensorManager = AccSensorManager(v_flag = false, buf_time = null)
    private lateinit var touchPointManager: TouchPointManager

    private val motionManager = MotionManager()

    enum class REQUEST_INTENT {
        SETTING
    }


    // Creating View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        touchPointManager = TouchPointManager(this)


        // ----- UI初期化 -----
        trackView = findViewById(R.id.trackView)
        trackView.setOnTouchListener { v, event ->  touchProcess(v, event)}

        demoView = findViewById(R.id.demoView)

        settingButton = findViewById(R.id.settingButton)
        settingButton.setOnClickListener {
            val intent = Intent(this, SettingMenuActivity::class.java)
//            startActivityForResult(intent, REQUEST_INTENT.SETTING.ordinal)
            forwardButton.isVisible = true
            forwardButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_show))

            backwardButton.isVisible = true
            backwardButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_show))

            presenButton.isVisible = true
            presenButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_show))
        }

        forwardButton = findViewById(R.id.forward_button)
        forwardButton.isVisible = false
        forwardButton.setOnClickListener {

        }

        backwardButton = findViewById(R.id.backward_button)
        backwardButton.isVisible = false
        backwardButton.setOnClickListener {

        }

        presenButton = findViewById(R.id.presen_button)
        presenButton.isVisible = false
        presenButton.setOnClickListener {

        }

        // ブロードキャストConnect
        receivedHostIp()
        sendBroadcast(this)
    }


    // Back to View
    override fun onResume() {
        super.onResume()

        touchPointManager.ipPortManager.reloadAddressInfo()

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
//                    Log.d("Activity","Sensor.TYPE_LIGHT :" + event.values[0].toString())
                    val lightParam = event.values[0]
                    if (lightParam < 5f) {
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
        val motion = motionManager.frame(event)

        if (motion != null) {
            when(motion.gesture) {
                GESTURE.FIRST -> {
                    this.touchPointManager.firstDown()
                }

                GESTURE.MOVE -> {
                    this.touchPointManager.moved(motion.x ?: 0f, motion.y ?: 0f)
                }

                GESTURE.L_CLICK -> {
                    this.touchPointManager.clickL()
                }

                GESTURE.R_CLICK -> {
                    this.touchPointManager.clickR()
                }

                GESTURE.SCROLL -> {
                    this.touchPointManager.scroll(motion.x ?: 0f, motion.y ?: 0f)
                }

                GESTURE.L_SWIPE -> {
                    this.touchPointManager.swipeL()
                }

                GESTURE.R_SWIPE -> {
                    this.touchPointManager.swipeR()
                }
            }
        }

        // ポインター更新
        demoView.x = event.x
        demoView.y = event.y

        return true
    }



    data class AccSensorManager(var v_flag: Boolean, var buf_time: Long?)

    data class TouchPoint(var x:Float, var y:Float)

    class TouchPointManager(val context: Context) {

        init {
            sensitiveReload()
        }

        val ipPortManager = IpPortManager(context)


        // 感度
        private var cursorMagnification = 100
        private var scrollMagnification = 100

        // 照度フラグ
        private var lightOn: Boolean = false


        private var forceStop: Boolean = false  // 強制停止フラグ

        fun firstDown() {
            val postJsonObj = JSONObject()
            postJsonObj.put("key", "first")
            runSocket(
                ipPortManager.getIP(),
                ipPortManager.getPort(),
                postJsonObj
            )
        }

        fun clickL() {
            val postJsonObj = JSONObject()
            postJsonObj.put("key", "click")
            runSocket(
                ipPortManager.getIP(),
                ipPortManager.getPort(),
                postJsonObj
            )

            Log.d("[MOTION DEBUG]", "[click Left]")
        }

        fun clickR() {
            val postJsonObj = JSONObject()
            postJsonObj.put("key", "two_fingers_click")
            runSocket(
                ipPortManager.getIP(),
                ipPortManager.getPort(),
                postJsonObj
            )

            Log.d("[MOTION DEBUG]", "[click Right]")
        }

        fun moved(x: Float, y: Float) {
            val postJsonObj = JSONObject()
            postJsonObj.put("key", "moved")

            postJsonObj.put("context", JSONObject().apply {
                put("x", x * (cursorMagnification.toFloat() / 100f))
                put("y", y * (cursorMagnification.toFloat() / 100f))
            })

            runSocket(
                ipPortManager.getIP(),
                ipPortManager.getPort(),
                postJsonObj
            )

            Log.d("[MOTION DEBUG]", "[move] x: %f, y: %f".format(x, y))
        }

        fun scroll(x: Float, y: Float) {
            val postJsonObj = JSONObject()
            postJsonObj.put("key", "scroll")
            postJsonObj.put("context", JSONObject().apply {
                put("x", (x*10 * (scrollMagnification.toFloat() / 100f)).toInt())
                put("y", (y*10 * (scrollMagnification.toFloat() / 100f)).toInt())
            })
            runSocket(
                ipPortManager.getIP(),
                ipPortManager.getPort(),
                postJsonObj
            )

            Log.d("[MOTION DEBUG]", "[scroll] x: %f, y: %f".format(x, y))
        }

        fun swipeL() {
            val postJsonObj = JSONObject()
            postJsonObj.put("key", "browser_back")
            runSocket(
                ipPortManager.getIP(),
                ipPortManager.getPort(),
                postJsonObj
            )

            Log.d("[MOTION DEBUG]", "[swipe Left]")
        }

        fun swipeR() {
            val postJsonObj = JSONObject()
            postJsonObj.put("key", "browser_forward")
            runSocket(
                ipPortManager.getIP(),
                ipPortManager.getPort(),
                postJsonObj
            )

            Log.d("[MOTION DEBUG]", "[swipe Right]")
        }

        // this function is called when Smart Phone is shake.
        fun shake() {
            if (forceStop) return

            val jsonObj = JSONObject()
            jsonObj.put("key", "shake")
            runSocket(
                ipPortManager.getIP(),
                ipPortManager.getPort(),
                jsonObj
            )
        }

        // 照度センサ（暗）
        fun gloomy(hide: Boolean) {
            if (forceStop) return

//            Log.d("TouchPointManager", "[gloomy()] called")

            if (hide && !this.lightOn) {
//                Log.d("TouchPointManager", "LockOn")
                val jsonObj = JSONObject()
                jsonObj.put("key", "lock")
                runSocket(
                    ipPortManager.getIP(),
                    ipPortManager.getPort(),
                    jsonObj
                )
                lightOn = true
            } else if (!hide && this.lightOn) {
//                Log.d("TouchPointManager", "LockOff")
                lightOn = false
            }

        }

        // get sensitive value from sharedPref
        private fun sensitiveReload() {
            val sharedPref = context.getSharedPreferences("Setting", Context.MODE_PRIVATE)
            this.cursorMagnification = sharedPref.getInt("cursor-sensitive", 100)
            this.scrollMagnification = sharedPref.getInt("scroll-sensitive", 100)
        }
    }
}
