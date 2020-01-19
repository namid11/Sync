package com.example.sync.Adapter

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sync.IpPortSettingActivity
import com.example.sync.R
import com.example.sync.SensitiveActivity

class SettingMenuAdapter(val context: AppCompatActivity): RecyclerView.Adapter<SettingMenuAdapter.DetectLabelsViewHolder>() {

    private val settingMenuDatas: List<MenuItemData> = listOf(
        MenuItemData(name = "Network", imgSrc = R.drawable.ui_network),
        MenuItemData(name = "Sensitive", imgSrc = R.drawable.ui_writing)
    )
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
        return settingMenuDatas.count()
    }

    override fun onBindViewHolder(holder: DetectLabelsViewHolder, position: Int) {
        val data = settingMenuDatas[position]
        holder.textView.text = data.name
        holder.uiImageView.setImageResource(data.imgSrc)
    }

    class DetectLabelsViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.setting_menu_item_textView)
        val uiImageView: ImageView = view.findViewById(R.id.setting_menu_item_uiView)
    }


    data class MenuItemData(val name: String, val imgSrc: Int)

}