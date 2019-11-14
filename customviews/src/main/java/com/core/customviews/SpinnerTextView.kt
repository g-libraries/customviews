package com.core.customviews

import android.content.Context
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListPopupWindow


class SpinnerTextView : DirtyDataTextView {

    var selection = 0
    lateinit var dataList: Array<String>
    lateinit var listPopupView: ListPopupWindow

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setDropdown(dataList: Array<String>, listItemId: Int, listener: (Int) -> Unit) {
        this.dataList = dataList
        listPopupView = ListPopupWindow(context)
        listPopupView.setAdapter(ArrayAdapter(context, listItemId, dataList) as ListAdapter?)
        listPopupView.anchorView = this
        setOnClickListener { listPopupView.show() }

        listPopupView.setOnItemClickListener { _, _, position, _ ->
            text = dataList[position]
            listPopupView.dismiss()
            this.selection = position
            listener(position)
        }
    }

    fun setCurrentItem(position: Int) {
        dataList.let {
            if (position >= it.size) return
            this.selection = position
            text = dataList[position]
        }
    }

}