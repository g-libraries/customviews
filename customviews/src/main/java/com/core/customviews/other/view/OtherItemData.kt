package com.core.customviews.other.view

import android.view.View
import androidx.annotation.DrawableRes
import com.core.customviews.other.FieldType

data class OtherItemData(
    @DrawableRes
    var drawable: Int,
    var title: String,
    var fieldType: FieldType,
    var onClick: (View) -> Unit = { },
    var onSwitchChanged: ((Boolean) -> Unit)? = null
)