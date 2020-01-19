package com.example.sync

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sync.Adapter.SettingMenuAdapter

class SettingMenuActivity : AppCompatActivity() {

    lateinit var settingRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_menu)

        // Set RecyclerView
        settingRecyclerView = findViewById(R.id.setting_recycler_view)
        settingRecyclerView.adapter = SettingMenuAdapter(this)
        settingRecyclerView.layoutManager = GridLayoutManager(this, 2)

    }
}
