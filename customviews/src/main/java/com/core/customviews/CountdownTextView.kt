package com.core.customviews

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CountdownTextView : AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setText(text: String, sec: Long, color: Int) {

        val builder = SpannableStringBuilder(text.plus(" ").plus(sec))

        val colorSpan = ForegroundColorSpan(color)

        builder.setSpan(colorSpan, text.length, builder.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

        setText(builder)
    }

}