package com.example.sync.CustomUI

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.sync.Manager.convertDp2Px
import com.example.sync.Manager.convertPx2Dp

class CardConstraintLayout(context: Context, attr: AttributeSet): ConstraintLayout(context, attr) {
    override fun setPressed(pressed: Boolean) {
        elevation = if (pressed) convertDp2Px(10f, context) else convertDp2Px(5f, context)

        super.setPressed(pressed)
    }
}