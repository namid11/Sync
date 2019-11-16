package com.example.sync

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text

class SettingActivity : AppCompatActivity() {

    private lateinit var ipAddressEditText: EditText
    private lateinit var portNumberEditText: EditText
    private lateinit var ipAddressButton: Button
    private lateinit var portNumberButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        ipAddressEditText = findViewById(R.id.ipAddressEditText)
        ipAddressButton = findViewById(R.id.ipAddressButton)
        portNumberEditText = findViewById(R.id.portNumberEditText)
        portNumberButton = findViewById(R.id.portNumberButton)


        val sharedPref = getSharedPreferences("ServerAddress", Context.MODE_PRIVATE)

        setResult(Activity.RESULT_CANCELED)

        ipAddressEditText.setText(sharedPref.getString("ip", "192.168.100.2"), TextView.BufferType.NORMAL)
        portNumberEditText.setText(sharedPref.getInt("port", 8080).toString(), TextView.BufferType.NORMAL)


        ipAddressButton.setOnClickListener {
            sharedPref.edit().putString("ip", ipAddressEditText.text.toString()).apply()
            setResult(Activity.RESULT_OK)
        }

        portNumberButton.setOnClickListener {
            sharedPref.edit().putInt("port", portNumberEditText.text.toString().toInt()).apply()
            setResult(Activity.RESULT_OK)
        }
    }
}
