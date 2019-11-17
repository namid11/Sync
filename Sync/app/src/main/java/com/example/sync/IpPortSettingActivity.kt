package com.example.sync

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class IpPortSettingActivity : AppCompatActivity() {

    private lateinit var ipAddressEditText: EditText
    private lateinit var portNumberEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ip_port_setting)

        // RESULTの初期設定
        setResult(Activity.RESULT_CANCELED)

        // ActionBarを設定
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        setTitle("IPアドレス ポート番号")

        val actionbar= supportActionBar
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        ipAddressEditText = findViewById(R.id.ipAddressEditText)
        portNumberEditText = findViewById(R.id.portNumberEditText)


        val sharedPref = getSharedPreferences("ServerAddress", Context.MODE_PRIVATE)

        ipAddressEditText.setText(sharedPref.getString("ip", "192.168.100.2"), TextView.BufferType.NORMAL)
        portNumberEditText.setText(sharedPref.getInt("port", 8080).toString(), TextView.BufferType.NORMAL)
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
}
