package com.core.customviews.profile.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class ProfileDataLayout : LinearLayout, IProfileDataLayout {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    lateinit var profileItems: ArrayList<ProfileItemView>

    private fun init(context: Context) {
        orientation = VERTICAL
    }

    override fun setInitData(
        profileDataList: ArrayList<ProfileItemData>,
        profileEditMode: ProfileEditModel
    ) {
        for (item in profileDataList) {
            val viewItem = ProfileItemView(context)

            viewItem.init(item)
            addView(viewItem)

            profileItems.add(viewItem)
        }
    }
}