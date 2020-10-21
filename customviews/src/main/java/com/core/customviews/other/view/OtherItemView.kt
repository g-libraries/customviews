package com.core.customviews.other.view

import android.content.Context
import android.util.AttributeSet
import com.core.customviews.R
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import kotlinx.android.synthetic.main.item_other.view.*


class OtherItemView : LinearLayout, IOtherItemView {

    var selected = 0
    lateinit var dataList: Array<String>
    lateinit var listPopupView: ListPopupWindow
    var itemLayout: Int = 0


    lateinit var otherItemData: OtherItemData
    lateinit var titleTV: TextView
    lateinit var iconIV: AppCompatImageView
    lateinit var switch: Switch

    var onCheckedListener: CompoundButton.OnCheckedChangeListener? = null

    constructor(
        context: Context?,
        otherItemData: OtherItemData,
        itemLayout: Int
    ) : super(
        context
    ) {
        init(otherItemData, itemLayout)
    }


    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun init(otherItemData: OtherItemData, itemLayout: Int) {
        this.otherItemData = otherItemData

        if (itemLayout != 0)
            View.inflate(context, itemLayout, this)
        else
            View.inflate(context, R.layout.item_other, this)

        titleTV = item_other_tv
        iconIV = item_other_iv
        switch = item_other_switch

        titleTV.text = otherItemData.title
        iconIV.setImageResource(otherItemData.drawable)

        if (otherItemData.onSwitchChanged == null) {
            setType(OtherItemType.DEFAULT)
        } else {
            setType(OtherItemType.SWITCH)
        }
    }

    private fun setType(type: OtherItemType) {
        when (type) {
            OtherItemType.DEFAULT -> {
                switch.visibility = View.GONE
            }
            OtherItemType.SWITCH -> {
                switch.visibility = View.VISIBLE

                onCheckedListener = CompoundButton.OnCheckedChangeListener { p0, p1 ->
                    otherItemData.onSwitchChanged?.invoke(p1)
                }

                switch.setOnCheckedChangeListener(onCheckedListener)
            }
        }
    }


    override fun changeSwitchState(state: Boolean) {
        if (state != switch.isChecked) {
            switch.setOnCheckedChangeListener(null)
            switch.toggle()
            switch.setOnCheckedChangeListener(onCheckedListener)
        }
    }
}

