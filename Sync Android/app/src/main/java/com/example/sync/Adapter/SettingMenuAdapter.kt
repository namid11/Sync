package com.example.sync.Adapter

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
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
import com.example.sync.SettingMenuActivity

data class SettingMenuItemData(val name: String, val imgSrc: Int, val clickListener: (View) -> Unit)

class SettingMenuAdapter(val context: AppCompatActivity, private val menuData: List<SettingMenuItemData>): RecyclerView.Adapter<SettingMenuAdapter.DetectLabelsViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetectLabelsViewHolder {
        val view = inflater.inflate(R.layout.setting_menu_item, parent, false)
        val viewHolder = SettingMenuAdapter.DetectLabelsViewHolder(view)

        view.setOnClickListener {
            (AnimatorInflater.loadAnimator(context, R.animator.card_view_animator) as AnimatorSet).apply {
                setTarget(it)
                start()
            }
            menuData[viewHolder.adapterPosition].clickListener(it)
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return menuData.count()
    }

    override fun onBindViewHolder(holder: DetectLabelsViewHolder, position: Int) {
        val data = menuData[position]
        holder.textView.text = data.name
        holder.uiImageView.setImageResource(data.imgSrc)
    }

    class DetectLabelsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.setting_menu_item_textView)
        val uiImageView: ImageView = view.findViewById(R.id.setting_menu_item_uiView)
    }

}