package com.example.sync.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sync.PresentationModeActivity
import com.example.sync.Manager.GESTURE
import com.example.sync.Manager.IpPortManager
import com.example.sync.Manager.MotionManager
import com.example.sync.Manager.runSocket
import com.example.sync.R
import org.json.JSONObject

class MotionpadFragment: Fragment() {

    private lateinit var motionpadView: View

    private lateinit var touchPointManager: TouchPointManager
    private val motionManager = MotionManager()

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        touchPointManager = TouchPointManager(context as Context)

        val view = inflater.inflate(R.layout.layout_motionpad, container, false)
        motionpadView = view.findViewById(R.id.motionpad_view)
        motionpadView.setOnTouchListener { v, event ->  touchProcess(v, event)}

        return view
    }

    override fun onResume() {
        super.onResume()

        touchPointManager.reload()
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
        private var cursorMagnification = 100f
        private var scrollMagnification = 100f

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
                put("x", x * (cursorMagnification))
                put("y", y * (cursorMagnification))
            })

            runSocket(
                ipPortManager.getIP(),
                ipPortManager.getPort(),
                postJsonObj
            )

            //Log.d("[MOTION DEBUG]", "[move] x: %f, y: %f".format(x, y))
        }

        fun scroll(x: Float, y: Float) {
            val postJsonObj = JSONObject()
            postJsonObj.put("key", "scroll")
            postJsonObj.put("context", JSONObject().apply {
                put("x", (x*10 * (scrollMagnification)).toInt())
                put("y", (y*10 * (scrollMagnification)).toInt())
            })
            runSocket(
                ipPortManager.getIP(),
                ipPortManager.getPort(),
                postJsonObj
            )

            //Log.d("[MOTION DEBUG]", "[scroll] x: %f, y: %f".format(x, y))
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
        fun sensitiveReload() {
            val sharedPref = context.getSharedPreferences("Setting", Context.MODE_PRIVATE)
            this.cursorMagnification = sharedPref.getFloat("cursor_sensitive", 1f)
            this.scrollMagnification = sharedPref.getFloat("scroll_sensitive", 1f)
        }

        fun reload() {
            sensitiveReload()
            ipPortManager.reloadAddressInfo()
        }
    }
}