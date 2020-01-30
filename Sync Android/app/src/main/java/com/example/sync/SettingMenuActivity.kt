package com.example.sync

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sync.Adapter.SettingMenuAdapter
import com.example.sync.Adapter.SettingMenuItemData
import com.example.sync.Fragment.ConnectSettingFragment
import com.example.sync.Fragment.SensitiveSettingFragment
import com.example.sync.Fragment.SettingMenuItemFragment
import com.example.sync.Manager.receivedHostIp
import java.security.Key

class SettingMenuActivity : AppCompatActivity(), SettingMenuItemFragment.OnCloseExecListener {

    private lateinit var settingRecyclerView: RecyclerView
    private lateinit var containerConstraintLayout: ConstraintLayout
    private lateinit var frameLayout: FrameLayout


    enum class FOCUSLAYOUT {
        MENU, NETWORK, SENSITIVE
    }
    private var focusLayout: FOCUSLAYOUT = FOCUSLAYOUT.MENU

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_menu)

        // find container layout
        containerConstraintLayout = findViewById(R.id.setting_container_layout)


        val settingMenuData: List<SettingMenuItemData> = listOf(
            SettingMenuItemData(name = "Network", imgSrc = R.drawable.ui_network, clickListener = menuItemNetworkListener),
            SettingMenuItemData(name = "Sensitive", imgSrc = R.drawable.ui_writing, clickListener = menuItemSensitiveListener),
            SettingMenuItemData(name = "Manual", imgSrc = R.drawable.ui_information, clickListener = menuItemManualListener)
        )

        // Set RecyclerView
        settingRecyclerView = findViewById(R.id.setting_menu_recycler_view)
        settingRecyclerView.adapter = SettingMenuAdapter(this, settingMenuData)
        settingRecyclerView.layoutManager = GridLayoutManager(this, 2)

        frameLayout = findViewById(R.id.setting_menu_fragment_layout)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            for (f in supportFragmentManager.fragments) {
                (f as SettingMenuItemFragment).hiddenItemView()
            }
            Log.d("[onKeyDown]", supportFragmentManager.fragments.count().toString())
            if (supportFragmentManager.fragments.count() != 0) return false
        }
        return super.onKeyDown(keyCode, event)
    }

    private val menuItemNetworkListener: (View) -> Unit = {
        focusLayout = FOCUSLAYOUT.NETWORK

        if (supportFragmentManager.findFragmentByTag("connectSetting") == null) { // フラグメントがすでに存在してないか確認
            supportFragmentManager.beginTransaction()
                .add(R.id.setting_menu_fragment_layout, ConnectSettingFragment(), "connectSetting")
                .commit()
        }
    }

    private val menuItemSensitiveListener: (View) -> Unit = {
//        focusLayout = FOCUSLAYOUT.SENSITIVE

        if (supportFragmentManager.findFragmentByTag("sensitiveSetting") == null) { // フラグメントがすでに存在してないか確認
            supportFragmentManager.beginTransaction()
                .add(R.id.setting_menu_fragment_layout, SensitiveSettingFragment(), "sensitiveSetting")
                .commit()
        }
    }

    private val menuItemManualListener: (View) -> Unit = {
        val customTabsIntent: CustomTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(this, Uri.parse("https://sync-f0887.firebaseapp.com/manual"))
    }

    override fun onCloseListener() {
        for (f in supportFragmentManager.fragments) {
            supportFragmentManager.beginTransaction()
                .remove(f)
                .commit()
        }
    }
}
