package com.example.sync

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sync.Adapter.SettingMenuAdapter
import com.example.sync.Adapter.SettingMenuItemData
import com.example.sync.Manager.receivedHostIp

class SettingMenuActivity : AppCompatActivity() {

    private lateinit var settingRecyclerView: RecyclerView
    private lateinit var containerConstraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_menu)

        // find container layout
        containerConstraintLayout = findViewById(R.id.setting_container_layout)


        val settingMenuData: List<SettingMenuItemData> = listOf(
            SettingMenuItemData(name = "Network", imgSrc = R.drawable.ui_network, clickListener = menuItemNetworkListener),
            SettingMenuItemData(name = "Sensitive", imgSrc = R.drawable.ui_writing, clickListener = menuItemSensitiveListener)
        )

        // Set RecyclerView
        settingRecyclerView = findViewById(R.id.setting_menu_recycler_view)
        settingRecyclerView.adapter = SettingMenuAdapter(this, settingMenuData)
        settingRecyclerView.layoutManager = GridLayoutManager(this, 2)
    }


    private val menuItemNetworkListener: (View) -> Unit = {
        val parentView = layoutInflater.inflate(R.layout.layout_connect_setting, containerConstraintLayout)
        val connectSettingView: View = parentView.findViewById(R.id.connect_setting_layout)
        connectSettingView.setOnTouchListener { _, _ -> true }
        connectSettingView.alpha = 0f
        connectSettingView.top = parentView.height
        ViewCompat.animate(connectSettingView).apply {
            duration = 500
            y(0f)
            alpha(1f)
            interpolator = DecelerateInterpolator()
            start()
        }

        val sharedPref = getSharedPreferences("ServerAddress", Context.MODE_PRIVATE)
        val ipAddressEditText: EditText = parentView.findViewById(R.id.address_setting_manual_place_ip_address_edit_text)
        val portNumberEditText: EditText = parentView.findViewById(R.id.address_setting_manual_place_port_number_edit_text)
        val machineNameTextView: TextView = parentView.findViewById(R.id.address_setting_machine_name_text_view)
        ipAddressEditText.setText(sharedPref.getString("ip", "192.168.100.2"), TextView.BufferType.NORMAL)
        portNumberEditText.setText(sharedPref.getInt("port", 8080).toString(), TextView.BufferType.NORMAL)

        // 自動接続ボタンイベント
        val autoConnectingButton: Button = parentView.findViewById(R.id.auto_connect_button)
        autoConnectingButton.setOnClickListener {
            val handler = Handler()
            // ブロードキャストConnect
            sharedPref.edit().putInt("port", 8080).apply()
            // 受信用サーバ待機
            receivedHostIp {
                handler.post {
                    ipAddressEditText.setText(it.getString("ip"))
                    machineNameTextView.text = it.getString("machineName")
                    Toast.makeText(this, "[%s]に接続されました".format(it.getString("ip")), Toast.LENGTH_SHORT).show()
                }
                sharedPref.edit().putString("ip", it.getString("ip")).apply()
            }
            // ブロードキャスト送信
            com.example.sync.Manager.sendBroadcast(this)
        }

        val manualConnectSwitch: Switch = parentView.findViewById(R.id.manual_connect_switch)
        manualConnectSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked) {
                true -> {
                    ipAddressEditText.isEnabled = true
                    portNumberEditText.isEnabled = true
                }
                false -> {
                    ipAddressEditText.isEnabled = false
                    portNumberEditText.isEnabled = false
                }
            }
        }

        val backButton: ImageButton = connectSettingView.findViewById(R.id.activity_finished_button)
        backButton.setOnClickListener {
            ViewCompat.animate(connectSettingView).apply {
                duration = 500
                y(parentView.height.toFloat())
                alpha(0f)
                interpolator = AccelerateInterpolator()
                setListener(
                    object: ViewPropertyAnimatorListener {
                        override fun onAnimationEnd(view: View?) {
                            (parentView as ViewGroup).removeView(connectSettingView)
                        }

                        override fun onAnimationCancel(view: View?) {
                        }

                        override fun onAnimationStart(view: View?) {
                        }
                    }
                )
                start()
            }

        }
    }

    private val menuItemSensitiveListener: (View) -> Unit = {
        val parentView = layoutInflater.inflate(R.layout.layout_sensitive_setting, containerConstraintLayout)
        val sensitiveSettingView: View = parentView.findViewById(R.id.sensitive_setting_layout)
        sensitiveSettingView.setOnTouchListener { _, _ -> true }
        sensitiveSettingView.alpha = 0f
        sensitiveSettingView.top = parentView.height
        ViewCompat.animate(sensitiveSettingView).apply {
            duration = 500
            y(0f)
            alpha(1f)
            interpolator = DecelerateInterpolator()
            start()
        }

        val cursorSensBar: SeekBar = parentView.findViewById(R.id.cursor_sensitive_bar)
        val scrollSensBar: SeekBar = parentView.findViewById(R.id.scroll_sensitive_bar)
        val cursorParamTextView: TextView = parentView.findViewById(R.id.cursor_sens_param_text_view)
        val scrollParamTextView: TextView = parentView.findViewById(R.id.scroll_sens_param_text_view)

        // sharedPrefからデータ呼び出し
        val sharedPref = getSharedPreferences("Setting", Context.MODE_PRIVATE)
        val magCursor = sharedPref.getFloat("cursor_sensitive", 1f)
        val magScroll = sharedPref.getFloat("scroll_sensitive", 1f)
        cursorSensBar.progress = (magCursor * 100f).toInt()
        scrollSensBar.progress = (magScroll * 100f).toInt()

        // 初期Display処理
        cursorParamTextView.text = "x%.1f".format(magCursor)
        scrollParamTextView.text = "x%.1f".format(magScroll)

        cursorSensBar.setOnSeekBarChangeListener(
            object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (progress < 50) {
                        seekBar?.progress = 50
                        sharedPref.edit().putFloat("cursor_sensitive", 0.5f).apply()
                    } else {
                        sharedPref.edit().putFloat("cursor_sensitive", progress.toFloat() / 100f).apply()
                        cursorParamTextView.text = "x%.1f".format(progress / 100f)
                    }
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
                    if (progress < 50) {
                        seekBar?.progress = 50
                        sharedPref.edit().putFloat("scroll_sensitive", 0.5f).apply()
                    } else {
                        sharedPref.edit().putFloat("scroll_sensitive", progress.toFloat() / 100f).apply()
                        scrollParamTextView.text = "x%.1f".format(progress / 100f)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            }
        )

        val backButton: ImageButton = sensitiveSettingView.findViewById(R.id.activity_finished_button)
        backButton.setOnClickListener {
            ViewCompat.animate(sensitiveSettingView).apply {
                duration = 500
                y(parentView.height.toFloat())
                alpha(0f)
                interpolator = AccelerateInterpolator()
                setListener(
                    object: ViewPropertyAnimatorListener {
                        override fun onAnimationEnd(view: View?) {
                            (parentView as ViewGroup).removeView(sensitiveSettingView)
                        }

                        override fun onAnimationCancel(view: View?) {
                        }

                        override fun onAnimationStart(view: View?) {
                        }

                    }
                )
                start()
            }
        }
    }
}
