package com.core.customviews.other.view

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.core.customviews.other.FieldType

data class OtherItemData(
    @DrawableRes
    var drawable: Int,
    var title: String,
    var fieldType: FieldType,
    @ColorRes
    var textColor: Int,
    @ColorRes
    var dividerBg: Int,
    var onClick: (View) -> Unit = { },
    @DrawableRes
    var amountBGColor: Int? = null,
    @ColorRes
    var amountTextColor: Int? = null,
    var onSwitchChanged: ((Boolean) -> Unit)? = null
)