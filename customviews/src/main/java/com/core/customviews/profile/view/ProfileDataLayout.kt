package com.core.customviews.profile.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.res.getResourceIdOrThrow
import com.core.customviews.R

class ProfileDataLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs),
    IProfileDataLayout {
    val profileItems: ArrayList<ProfileItemView> = arrayListOf()
    var itemLayout: Int = 0
    lateinit var chagnedListener: OnChangedListener

    private fun init(context: Context, attrs: AttributeSet) {
        orientation = VERTICAL

        val a = context.obtainStyledAttributes(attrs, R.styleable.ProfileDataLayout)


        try {
            itemLayout = a.getResourceIdOrThrow(R.styleable.ProfileDataLayout_item_layout)
        } finally {
            a.recycle()
        }

    }

    override fun setInitData(
        profileDataList: ArrayList<ProfileItemData>,
        profileEditMode: ProfileEditModel,
        listener: OnChangedListener
    ) {
        chagnedListener = listener

        for (item in profileDataList) {
            val viewItem = ProfileItemView(context, item, itemLayout, chagnedListener)

            addView(viewItem)
            profileItems.add(viewItem)
        }
    }

    init {
        init(context, attrs)
    }
}