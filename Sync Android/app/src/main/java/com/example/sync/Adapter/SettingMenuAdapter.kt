package com.example.sync.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sync.IpPortSettingActivity
import com.example.sync.R
import com.example.sync.SensitiveActivity
import org.json.JSONArray

class SettingMenuAdapter(val context: AppCompatActivity): RecyclerView.Adapter<SettingMenuAdapter.DetectLabelsViewHolder>() {

    private val settingMenuLabels: List<String> = listOf("ネットワーク設定", "感度調整")
    private val inflater = LayoutInflater.from(context)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetectLabelsViewHolder {
        val view = inflater.inflate(R.layout.setting_menu_item, parent, false)
        val viewHolder = SettingMenuAdapter.DetectLabelsViewHolder(view)

        view.setOnClickListener {
            Log.d("{Click Item}", "position: %d".format(viewHolder.adapterPosition))

            when (val position = viewHolder.adapterPosition) {
                0 -> {
                    val intent = Intent(context, IpPortSettingActivity::class.java)
                    context.startActivityForResult(intent, position)
                }
                1 -> {
                    val intent = Intent(context, SensitiveActivity::class.java)
                    context.startActivityForResult(intent, position)
                }
            }
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return settingMenuLabels.count()
    }

    override fun onBindViewHolder(holder: DetectLabelsViewHolder, position: Int) {
        holder.textView.text = settingMenuLabels[position]
    }

    class DetectLabelsViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.setting_menu_item_textView)
    }

}