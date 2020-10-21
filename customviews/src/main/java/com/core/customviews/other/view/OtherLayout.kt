package com.core.customviews.other.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.view.updateLayoutParams
import com.core.customviews.R
import com.core.customviews.other.FieldType
import java.util.*
import kotlin.collections.ArrayList

class OtherLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs),
    IOtherLayout {

    init {
        init(context, attrs)
    }

    val otherViewItems: EnumMap<FieldType, OtherItemView> = EnumMap(FieldType::class.java)
    var itemLayout: Int = 0

    private fun init(context: Context, attrs: AttributeSet) {
        orientation = VERTICAL

        val a = context.obtainStyledAttributes(attrs, R.styleable.OtherLayout)


        try {
            itemLayout = a.getResourceIdOrThrow(R.styleable.OtherLayout_ol_item_layout)
        } finally {
            a.recycle()
        }
    }


    override fun setInitData(
        otherDataItems: ArrayList<OtherItemData>
    ) {
        removeAllViews()
        otherViewItems.clear()

        for (item in otherDataItems) {
            val viewItem = OtherItemView(context, item, itemLayout)
            val divider: View = LayoutInflater.from(context).inflate(R.layout.divider, this)

            divider.setBackgroundColor(ContextCompat.getColor(context, item.dividerBg))
            viewItem.titleTV.setTextColor(ContextCompat.getColor(context, item.textColor))

            addView(viewItem)
            addView(divider)

            otherViewItems[item.fieldType] = viewItem
        }
    }

    override fun getItemView(fieldType: FieldType): IOtherItemView = otherViewItems[fieldType]!!

}