package com.core.customviews.other.view

import androidx.annotation.DrawableRes
import com.core.customviews.other.FieldType

data class OtherItemData(
    @DrawableRes
    var drawable: Int,
    var title: String,
    var fieldType: FieldType,
    var onClick: () -> Unit = { },
    var onSwitchChanged: ((Boolean) -> Unit)? = null
)