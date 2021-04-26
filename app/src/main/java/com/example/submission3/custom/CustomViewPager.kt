package com.example.submission3.custom


import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager


/**
 * Created by vihaan on 1/9/15.
 */
class CustomViewPager : ViewPager {
    private var mCurrentView: View? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpecParam: Int) {
        var heightMeasureSpec = heightMeasureSpecParam
        if (mCurrentView == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        var height = 0
        mCurrentView?.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        val h = mCurrentView!!.measuredHeight
        if (h > height) height = h
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun measureCurrentView(currentView: View?) {
        mCurrentView = currentView
        requestLayout()
    }
}