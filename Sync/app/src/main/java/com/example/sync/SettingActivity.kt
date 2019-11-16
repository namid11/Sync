package com.example.sync

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

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


    }
}
