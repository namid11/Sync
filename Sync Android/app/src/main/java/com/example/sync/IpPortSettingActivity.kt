package com.example.sync

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.sync.Manager.*
import org.json.JSONObject

class IpPortSettingActivity : AppCompatActivity() {

    private lateinit var ipAddressEditText: EditText
    private lateinit var portNumberEditText: EditText
    private lateinit var autoConnectingButton: Button
    private lateinit var machineNameTextView: TextView
    private lateinit var manualConnectSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ip_port_setting)

        // RESULTの初期設定
        setResult(Activity.RESULT_CANCELED)

        // ActionBarを設定
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        this.title = ""

        val actionbar= supportActionBar
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        val sharedPref = getSharedPreferences("ServerAddress", Context.MODE_PRIVATE)
        ipAddressEditText = findViewById(R.id.ipAddressEditText)
        portNumberEditText = findViewById(R.id.portNumberEditText)
        machineNameTextView = findViewById(R.id.address_setting_machine_name_textView)
        ipAddressEditText.setText(sharedPref.getString("ip", "192.168.100.2"), TextView.BufferType.NORMAL)
        portNumberEditText.setText(sharedPref.getInt("port", 8080).toString(), TextView.BufferType.NORMAL)

        // 自動接続ボタンイベント
        autoConnectingButton = findViewById(R.id.autoConnectingButton)
        autoConnectingButton.setOnClickListener {
            val handler = Handler()
            // ブロードキャストConnect
            sharedPref.edit().putInt("port", 8080).apply()
            receivedHostIp {
                handler.post {
                    ipAddressEditText.setText(it.getString("ip"))
                    machineNameTextView.text = it.getString("machineName")
                    Toast.makeText(this, "[%s]に接続されました".format(it.getString("ip")), Toast.LENGTH_SHORT).show()
                }
                sharedPref.edit().putString("ip", it.getString("ip")).apply()
            }
            sendBroadcast(this)
        }

        manualConnectSwitch = findViewById(R.id.manualConnectSwitch)
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
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ip_port_setting_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            val id = item.itemId

            when (id) {

                // 更新処理
                R.id.upload_setting -> {
                    val sharedPref = getSharedPreferences("ServerAddress", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("ip", ipAddressEditText.text.toString()).apply()
                    setResult(Activity.RESULT_OK)
                    sharedPref.edit().putInt("port", portNumberEditText.text.toString().toInt()).apply()
                    setResult(Activity.RESULT_OK)
                    return true
                }


                // 戻るボタン処理
                android.R.id.home -> {
                    this.finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private val setConnetctedData = fun(ipAddress: String, machineName: String) {
        ipAddressEditText.setText(ipAddress)
        machineNameTextView.text = machineName
    }
}
