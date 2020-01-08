package com.example.sync

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import org.w3c.dom.Text

class SensitiveActivity : AppCompatActivity() {

    private lateinit var cursorSensBar: SeekBar
    private lateinit var scrollSensBar: SeekBar
    private lateinit var cursorParamTextView: TextView
    private lateinit var scrollParamTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensitive)

        cursorSensBar = findViewById(R.id.cursor_sensitive_bar)
        scrollSensBar = findViewById(R.id.scroll_sensitive_bar)
        cursorParamTextView = findViewById(R.id.cursor_sens_param_textView)
        scrollParamTextView = findViewById(R.id.scroll_sens_param_textView)

        // sharedPrefからデータ呼び出し
        val sharedPref = getSharedPreferences("Setting", Context.MODE_PRIVATE)
        val magCursor = sharedPref.getInt("cursor-sensitive", 100)
        val magScroll = sharedPref.getInt("scroll-sensitive", 100)
        cursorSensBar.progress = magCursor
        scrollSensBar.progress = magScroll

        // 初期Disp処理
        dispCursorParam(magCursor)
        dispScrollParam(magScroll)

        // シークバーイベント処理
        cursorSensBar.setOnSeekBarChangeListener(
            object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (progress < 50) return
                    sharedPref.edit().putInt("cursor-sensitive", progress).apply()
                    dispCursorParam(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            }
        )

        scrollSensBar.setOnSeekBarChangeListener(
            object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    sharedPref.edit().putInt("scroll-sensitive", progress).apply()
                    dispScrollParam(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            }
        )

    }


    private fun dispCursorParam(param: Int) {
        cursorParamTextView.text = "x%.1f".format(param.toFloat() / 100f)
    }

    private fun dispScrollParam(param: Int) {
        scrollParamTextView.text = "x%.1f".format(param.toFloat() / 100f)
    }
}
