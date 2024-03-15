package com.shamanshs.russian_checkers.tools

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class GridViewItem(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    ImageView(context, attrs, defStyle) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}