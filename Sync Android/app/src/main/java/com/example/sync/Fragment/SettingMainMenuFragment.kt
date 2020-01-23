package com.example.sync.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sync.Adapter.SettingMenuAdapter
import com.example.sync.Adapter.SettingMenuItemData
import com.example.sync.R

class SettingMainMenuFragment: Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_setting_menu, container, false)
        val settingMenuRecyclerView = view.findViewById<RecyclerView>(R.id.setting_menu_recycler_view)
        if (context != null) {
        }
        return view
    }

}