package com.example.sync.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sync.R
import com.example.sync.SettingMenuActivity
import java.lang.RuntimeException

class SensitiveSettingFragment: SettingMenuItemFragment() {

    private lateinit var layoutView: View
    private lateinit var sensitiveSettingView: View
    private lateinit var cursorSensBar:SeekBar
    private lateinit var scrollSensBar:SeekBar
    private lateinit var cursorParamTextView: TextView
    private lateinit var scrollParamTextView: TextView

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        layoutView = layoutInflater.inflate(R.layout.layout_sensitive_setting, container, false)
//        sensitiveSettingView = layoutView.findViewById(R.id.sensitive_setting_layout)

        cursorSensBar = layoutView.findViewById(R.id.cursor_sensitive_bar)
        cursorSensBar.tag = "cursor_sensitive"
        scrollSensBar = layoutView.findViewById(R.id.scroll_sensitive_bar)
        scrollSensBar.tag = "scroll_sensitive"
        cursorParamTextView = layoutView.findViewById(R.id.cursor_sens_param_text_view)
        scrollParamTextView = layoutView.findViewById(R.id.scroll_sens_param_text_view)

        // sharedPrefからデータ呼び出し
        val sharedPref = context?.getSharedPreferences("Setting", Context.MODE_PRIVATE)
        if (sharedPref != null) {
            val magCursor = sharedPref.getFloat("cursor_sensitive", 1f)
            val magScroll = sharedPref.getFloat("scroll_sensitive", 1f)
            cursorSensBar.progress = (magCursor * 100f).toInt()
            scrollSensBar.progress = (magScroll * 100f).toInt()

            // 初期Display処理
            cursorParamTextView.text = "x%.1f".format(magCursor)
            scrollParamTextView.text = "x%.1f".format(magScroll)
        }

        cursorSensBar.setOnSeekBarChangeListener(cursorSeekBarChangeListener)

        scrollSensBar.setOnSeekBarChangeListener(scrollSeekBarChangeListener)

        val backButton: ImageButton = layoutView.findViewById(R.id.activity_finished_button)
        backButton.setOnClickListener {
            hiddenItemView()
        }

        return layoutView
    }

    override fun onResume() {
        super.onResume()

        showItemView()
    }


    private val cursorSeekBarChangeListener: SeekBar.OnSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            val sharedPref = context?.getSharedPreferences("Setting", Context.MODE_PRIVATE)
            if (sharedPref != null) {
                if (progress < 50) {
                    seekBar?.progress = 50
                    sharedPref.edit().putFloat("cursor_sensitive", 0.5f).apply()
                } else {
                    sharedPref.edit().putFloat("cursor_sensitive", progress.toFloat() / 100f).apply()
                }
                cursorParamTextView.text = "x%.1f".format(progress / 100f)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }

    }

    private val scrollSeekBarChangeListener: SeekBar.OnSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            val sharedPref = context?.getSharedPreferences("Setting", Context.MODE_PRIVATE)
            if (sharedPref != null) {
                if (progress < 50) {
                    seekBar?.progress = 50
                    sharedPref.edit().putFloat("scroll_sensitive", 0.5f).apply()
                } else {
                    sharedPref.edit().putFloat("scroll_sensitive", progress.toFloat() / 100f).apply()
                }
                scrollParamTextView.text = "x%.1f".format(progress / 100f)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }

    }
}