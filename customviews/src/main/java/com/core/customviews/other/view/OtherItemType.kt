package com.core.customviews.other.view

import android.text.TextWatcher
import android.widget.EditText
import com.core.customviews.other.FieldType
import com.core.customviews.profile.ProfileItem
import com.core.customviews.profile.view.OnChangedListener
import com.core.customviews.profile.view.ProfileEditModel
import com.core.customviews.profile.view.ProfileItemData

enum class OtherItemType {
    DEFAULT, SWITCH
}

interface IOtherLayout {
    fun setInitData(
        otherDataItems: ArrayList<OtherItemData>
    )

    fun udpate(otherDataItems: ArrayList<OtherItemData>)

    fun getItemView(fieldType: FieldType): IOtherItemView
}

interface IOtherItemView {
    fun changeSwitchState(state: Boolean)
}
