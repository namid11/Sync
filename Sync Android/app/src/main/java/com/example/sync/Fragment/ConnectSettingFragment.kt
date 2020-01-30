package com.example.sync.Fragment

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.sync.Manager.receivedHostIp
import com.example.sync.R

class ConnectSettingFragment: SettingMenuItemFragment() {

    private lateinit var layoutView: View
    private lateinit var connectSettingView: View
    private lateinit var ipAddressEditText: EditText
    private lateinit var portNumberEditText: EditText
    private lateinit var machineNameTextView: TextView
    private lateinit var manualConnectSwitch: Switch
    private lateinit var autoConnectingButton: Button

    val autoConnectOperation: () -> Unit = {
        val sharedPref = context?.getSharedPreferences("ServerAddress", Context.MODE_PRIVATE)
        if (sharedPref != null) {
            // show progress dialog
            val dialogView = layoutInflater.inflate(R.layout.dialog_connecting_progress, null)
            val dialogBuilder = AlertDialog.Builder(context!!).apply {
                setView(dialogView) // Viewをセット
                setCancelable(true) // dialog以外をタップしたら、消える
            }
            val progressBar = dialogView.findViewById<ProgressBar>(R.id.dialog_progress_progress_bar)
            val imageView = dialogView.findViewById<ImageView>(R.id.dialog_progress_check_image_view)
            val titleTextView = dialogView.findViewById<TextView>(R.id.dialog_progress_title_text_box)
            val okButton = dialogView.findViewById<Button>(R.id.ok_button)
            val msgTextView = dialogView.findViewById<TextView>(R.id.dialog_progress_msg_text_view)
            msgTextView.isVisible = false
            okButton.isEnabled = false
            val alertDialog = dialogBuilder.show()// show

            val handler = Handler()
            // ブロードキャストConnect
            sharedPref.edit().putInt("port", 8080).apply()
            // 受信用サーバ待機
            receivedHostIp(
                resolve = {
                    sharedPref.edit().putString("ip", it.getString("ip"))?.apply()
                    handler.post {
                        Thread.sleep(1500)

                        ipAddressEditText.setText(it.getString("ip"))
                        machineNameTextView.text = it.getString("machineName")

                        progressBar.isVisible = false
                        titleTextView.text = "Connecting Success !"
                        okButton.isEnabled = true
                        okButton.setOnClickListener {
                            alertDialog.dismiss()
                        }
                        imageView.setImageResource(R.drawable.ani_check_mark)
                        val animatedDrawable = imageView.drawable as AnimatedVectorDrawable
                        if (!animatedDrawable.isRunning) {
                            animatedDrawable.start()
                        }
                    }
                },
                reject = {
                    handler.post {
                        progressBar.isVisible = false
                        okButton.isEnabled = true
                        okButton.setOnClickListener {
                            alertDialog.dismiss()
                        }
                        titleTextView.text = "Connecting Fail ..."
                        msgTextView.isVisible = true
                        msgTextView.text = "再接続してみてください"
                    }
                }
            )

            // ブロードキャスト送信
            com.example.sync.Manager.sendBroadcast(context!!)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutView = layoutInflater.inflate(R.layout.layout_connect_setting, container, false)
        connectSettingView = layoutView.findViewById(R.id.connect_setting_layout)

        val sharedPref = context?.getSharedPreferences("ServerAddress", Context.MODE_PRIVATE)
        if (sharedPref != null) {
            ipAddressEditText = layoutView.findViewById(R.id.address_setting_manual_place_ip_address_edit_text)
            ipAddressEditText.setText(sharedPref.getString("ip", "192.168.100.2"), TextView.BufferType.NORMAL)
            ipAddressEditText.isEnabled = false
            ipAddressEditText.tag = "ip"
            ipAddressEditText.setOnEditorActionListener(editTextEditedListener)

            portNumberEditText = layoutView.findViewById(R.id.address_setting_manual_place_port_number_edit_text)
            portNumberEditText.setText(sharedPref.getInt("port", 8080).toString(), TextView.BufferType.NORMAL)
            portNumberEditText.isEnabled = false
            portNumberEditText.tag = "port"
            portNumberEditText.setOnEditorActionListener(editTextEditedListener)


            machineNameTextView = layoutView.findViewById(R.id.address_setting_machine_name_text_view)

            autoConnectingButton = layoutView.findViewById(R.id.auto_connect_button)
            autoConnectingButton.setOnClickListener(autoConnectingButtonListener)

            manualConnectSwitch = layoutView.findViewById(R.id.manual_connect_switch)
            manualConnectSwitch.setOnCheckedChangeListener(manualConnectSwitchChangedListener)
        }

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


    private val autoConnectingButtonListener: View.OnClickListener = View.OnClickListener {
        val sharedPrefDialog = context?.getSharedPreferences("DialogDisplay", Context.MODE_PRIVATE)
        if (sharedPrefDialog != null) {
            if (sharedPrefDialog.getBoolean("auto_connect", true)) {
                val dialogView = layoutInflater.inflate(R.layout.dialog_auto_connection, null)
                val dialogBuilder = AlertDialog.Builder(context!!).apply {
                    setView(dialogView)
                    setIcon(R.mipmap.ic_launcher)
                    setCancelable(true)
                }
                val alertDialog = dialogBuilder.show()
                dialogView.findViewById<Button>(R.id.dialog_auto_connection_ok_button).setOnClickListener {
                    val radioButton: RadioButton = dialogView.findViewById(R.id.display_again_radio_button)
                    if (radioButton.isChecked) sharedPrefDialog.edit().putBoolean("auto_connect", false).apply()
                    alertDialog.dismiss()
                    autoConnectOperation()
                }
            } else {
                autoConnectOperation()
            }
        }
    }

    private val manualConnectSwitchChangedListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
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

    private val editTextEditedListener = TextView.OnEditorActionListener { v, actionId, _ ->
        val sharedPref = context?.getSharedPreferences("ServerAddress", Context.MODE_PRIVATE)
        if (sharedPref != null) {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    v.windowToken,
                    0
                )
                sharedPref.edit().putString(v.tag as String, v.text.toString()).apply()
                Toast.makeText(context, "更新完了", Toast.LENGTH_SHORT).show()
                return@OnEditorActionListener true
            }
            return@OnEditorActionListener false
        }

        return@OnEditorActionListener false
    }
}