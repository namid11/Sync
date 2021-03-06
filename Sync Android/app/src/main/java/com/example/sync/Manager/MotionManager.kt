package com.example.sync.Manager

import android.util.Log
import android.view.MotionEvent
import com.example.sync.Fragment.MotionpadFragment
import com.example.sync.PresentationModeActivity
import java.lang.Exception
import kotlin.math.abs
import com.example.sync.Fragment.MotionpadFragment.TouchPoint

enum class GESTURE {
    FIRST, L_CLICK, R_CLICK, MOVE, SCROLL, L_SWIPE, R_SWIPE
}
enum class DEFINEGESTURE {
    MOVE, TWO, SWIPE, TRIPLE
}

data class MotionData (
    val gesture: GESTURE,
    val x: Float? = null,
    val y: Float? = null
)

data class EventData (
    val x: Float,
    val y: Float,
    val time: Long,
    val action: Int,
    val pointerCount: Int,
    val touchPoints: List<TouchPoint>
)

fun MotionEventToEventData(event: MotionEvent): EventData {
    val touchPoints: MutableList<TouchPoint> = mutableListOf()
    for (i in 0 until event.pointerCount) {
        val pid = event.getPointerId(i)
        try {
            touchPoints.add(
                TouchPoint(
                    x = event.getX(
                        pid
                    ), y = event.getY(pid)
                )
            )
        } catch (e: Exception) {
            Log.e("MotionEventToEventData", e.message)
        }
    }

    return EventData(
        x = event.x,
        y = event.y,
        time = event.eventTime,
        action = event.action,
        pointerCount = event.pointerCount,
        touchPoints = touchPoints
    )
}

class MotionManager {

    private lateinit var firstMotion: EventData       // event data of first tap
    private lateinit var previousMotion: EventData    // data of previous event
    private var defineGesture: DEFINEGESTURE? = null    // variable defined the motion
    private val scrollManager =
        ScrollManager()

    // get MotionEvent
    fun frame(event: MotionEvent): MotionData? {
        val action = event.action
        val finger_num = event.pointerCount

        when (action) {
            MotionEvent.ACTION_MOVE -> {
                when (finger_num) {
                    1 -> {
                        if (defineGesture != DEFINEGESTURE.TWO &&
                            defineGesture != DEFINEGESTURE.SWIPE &&
                            defineGesture != DEFINEGESTURE.TRIPLE
                        ) {      // 確定ジェスチャーがnullなら

                            defineGesture =
                                DEFINEGESTURE.MOVE                                                  // 設定
                            val disX = event.x - firstMotion.x
                            val disY = event.y - firstMotion.y
                            return MotionData(
                                GESTURE.MOVE,
                                x = disX,
                                y = disY
                            )
                        }
                    }
                    2 -> {
                        if (defineGesture != DEFINEGESTURE.SWIPE &&
                            defineGesture != DEFINEGESTURE.TRIPLE
                        ) {// 確定ジェスチャーがtriple以外なら

                            defineGesture =
                                DEFINEGESTURE.TWO       // 設定
                            val twoTapMotion = scrollManager.move(event)    // 二本指Moveのときの動作を決定
                            if (twoTapMotion != null) {     // SWIPEのときは、DEFINEにし、次の動作を停止
                                if (twoTapMotion.gesture == GESTURE.L_SWIPE || twoTapMotion.gesture == GESTURE.R_SWIPE) {
                                    defineGesture =
                                        DEFINEGESTURE.SWIPE
                                }
                            }
                            return twoTapMotion
                        }
                    }
                    3 -> {
                        // 確定ジェスチャーがなんであろうと、設定OK
                        defineGesture =
                            DEFINEGESTURE.TRIPLE
                    }
                }
            }

            MotionEvent.ACTION_DOWN -> {
                when (finger_num) {
                    1 -> {      // 全てはここから
                        firstMotion =
                            MotionEventToEventData(event)
//                        previousMotion = MotionEventToEventData(event)
                        return MotionData(
                            GESTURE.FIRST
                        )
                    }
                    2 -> return null // <- 多分ありえない
                }
            }

            MotionEvent.ACTION_UP -> {
                val tapLag = event.eventTime - firstMotion.time

                when (finger_num) {
                    1 -> {
                        when (defineGesture) {
                            DEFINEGESTURE.MOVE -> {
                                if (tapLag <= 150) {
                                    reset()
                                    scrollManager.reset()
                                    return MotionData(
                                        GESTURE.L_CLICK
                                    )
                                }
                            }
                            DEFINEGESTURE.TWO -> {
                                if (tapLag <= 100) {
                                    reset()
                                    scrollManager.reset()
                                    return MotionData(
                                        GESTURE.R_CLICK
                                    )
                                }
                            }
                            DEFINEGESTURE.TRIPLE -> {

                            }
                        }
                    }
                    2 -> {
                        // ここもこない
                    }
                }

                reset()
                scrollManager.reset()
            }
        }

        return null
    }


    private fun reset() {
        defineGesture = null
    }



    class ScrollManager {
        private var firstScrl: EventData? = null
        private var previousScrl: EventData? = null

        private var counter = 0
        private var scrollOriginPoint: EventData? = null

        fun move(event: MotionEvent): MotionData? {
            if(counter <= 10) counter += 1
            if(counter == 10) {
                scrollOriginPoint = MotionEventToEventData(event)
                counter += 1
            }

            if (firstScrl != null && previousScrl != null) {
                val pid1 = event.getPointerId(0)
                val pid2 = event.getPointerId(1)
                val disX1 = event.getX(pid1) - previousScrl!!.touchPoints[pid1].x
                val disY1 = event.getY(pid1) - previousScrl!!.touchPoints[pid1].y
                val disX2 = event.getX(pid2) - previousScrl!!.touchPoints[pid2].x
                val disY2 = event.getY(pid2) - previousScrl!!.touchPoints[pid2].y
                val disX = (disX1 + disX2) / 2f
                val disY = (disY1 + disY2) / 2f

                Log.d("[Motion DEBUG]", disX.toString())
                previousScrl =
                    MotionEventToEventData(event)


                // 数値の一時的な増大に対する対策
                if (disX*disX + disY*disY >= 40000) {
                    Log.d("[Motion DEBUG]", "[scroll-remove]")
                    return null
                }

                if (counter >= 10) {    // 最初の誤動作部分は除去
                    val disX1ToFirst = event.getX(pid1) - scrollOriginPoint!!.touchPoints[pid1].x
                    val disX2ToFirst = event.getX(pid2) - scrollOriginPoint!!.touchPoints[pid2].x
                    val time = (event.eventTime - scrollOriginPoint!!.time).toFloat()
                    var v = 0f
                    if (time != 0f) {
                        v = ((disX1ToFirst + disX2ToFirst) / 2f) / (event.eventTime - scrollOriginPoint!!.time).toFloat()
                        Log.d("[Motion DEBUG]", "v: $v")
                    }
                    return if (abs(v) >= 4f) {
                        if (disX > 0) MotionData(
                            GESTURE.L_SWIPE
                        ) else MotionData(GESTURE.R_SWIPE)
                    } else {
                        MotionData(
                            GESTURE.SCROLL,
                            x = disX,
                            y = disY
                        )
                    }
                }


            } else {
                firstScrl =
                    MotionEventToEventData(event)
                previousScrl =
                    MotionEventToEventData(event)
            }

            return null
        }

        fun reset() {
            counter = 0
        }
    }

}