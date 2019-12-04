package com.core.customviews

import android.text.TextPaint
import android.text.style.UpdateAppearance
import android.text.style.CharacterStyle

class ColoredUnderlineSpan(private val mColor: Int) : CharacterStyle(), UpdateAppearance {

    override fun updateDrawState(tp: TextPaint) {
        try {
            val method = TextPaint::class.java.getMethod(
                "setUnderlineText",
                Integer.TYPE,
                java.lang.Float.TYPE
            )

            tp.color = mColor
            method.invoke(tp, mColor, 2f)
        } catch (e: Exception) {
            tp.isUnderlineText = true
        }

    }
}