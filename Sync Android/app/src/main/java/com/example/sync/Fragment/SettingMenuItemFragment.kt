package com.example.sync.Fragment

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.fragment.app.Fragment
import com.example.sync.SettingMenuActivity
import java.lang.RuntimeException

open class SettingMenuItemFragment: Fragment() {

    interface OnCloseExecListener {
        fun onCloseListener()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context !is OnCloseExecListener) {
            throw RuntimeException("リスナーを実装してください")
        }
    }

    fun showItemView() {
        val layoutView = if (view != null) view!! else return
        layoutView.setOnTouchListener { _, _ -> true }
        layoutView.alpha = 0f
        layoutView.top = 500
        ViewCompat.animate(layoutView).apply {
            duration = 500
            y(0f)
            alpha(1f)
            interpolator = DecelerateInterpolator()
            start()
        }
    }

    fun hiddenItemView() {
        val layoutView = if (view != null) view!! else return
        ViewCompat.animate(layoutView).apply {
            duration = 500
            y(layoutView.height * 0.5f)
            alpha(0f)
            interpolator = AccelerateInterpolator()
            setListener(
                object: ViewPropertyAnimatorListener {
                    override fun onAnimationEnd(view: View?) {
                        val listener = context as? OnCloseExecListener
                        listener?.onCloseListener()
                    }

                    override fun onAnimationCancel(view: View?) {
                    }

                    override fun onAnimationStart(view: View?) {
                    }
                }
            )
            start()
        }
    }

}