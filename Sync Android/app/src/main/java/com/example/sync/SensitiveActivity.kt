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

        // シークバーイベント処理
        cursorSensBar.setOnSeekBarChangeListener(
            object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (progress < 50) return
                    sharedPref.edit().putFloat("cursor_sensitive", progress.toFloat() / 100f).apply()
                    dispCursorParam(progress.toFloat())
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
                    sharedPref.edit().putFloat("scroll_sensitive", progress.toFloat() / 100f).apply()
                    dispScrollParam(progress.toFloat())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            }
        )

    }

    override fun onResume() {
        super.onResume()

        // sharedPrefからデータ呼び出し
        val sharedPref = getSharedPreferences("Setting", Context.MODE_PRIVATE)
        val magCursor = sharedPref.getFloat("cursor_sensitive", 1f)
        val magScroll = sharedPref.getFloat("scroll_sensitive", 1f)
        cursorSensBar.progress = (magCursor * 100f).toInt()
        scrollSensBar.progress = (magScroll * 100f).toInt()

        // 初期Display処理
        dispCursorParam(magCursor * 100f)
        dispScrollParam(magScroll * 100f)
    }


    private fun dispCursorParam(param: Float) {
        cursorParamTextView.text = "x%.1f".format(param / 100f)
    }

    private fun dispScrollParam(param: Float) {
        scrollParamTextView.text = "x%.1f".format(param / 100f)
    }
}
