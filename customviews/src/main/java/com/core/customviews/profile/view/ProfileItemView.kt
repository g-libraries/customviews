package com.core.customviews.profile.view

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListPopupWindow
import androidx.appcompat.widget.AppCompatEditText
import com.core.customviews.R
import com.core.customviews.profile.ProfileItem
import timber.log.Timber
import androidx.core.view.ViewCompat.setBackgroundTintList
import android.content.res.ColorStateList
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.item_profile.view.*


class ProfileItemView : ConstraintLayout, ProfileItem {

    var selected = 0
    lateinit var dataList: Array<String>
    lateinit var listPopupView: ListPopupWindow
    var itemLayout: Int = 0


    lateinit var profileItemData: ProfileItemData
    lateinit var editText: AppCompatEditText
    lateinit var iconIV: AppCompatImageView
    lateinit var changedListener: OnChangedListener

    constructor(
        context: Context?,
        profileItemData: ProfileItemData,
        itemLayout: Int,
        changedListener: OnChangedListener
    ) : super(
        context
    ) {
        init(profileItemData, itemLayout)

        this.changedListener = changedListener
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun init(profileItemData: ProfileItemData, itemLayout: Int) {
        this.profileItemData = profileItemData

        if (itemLayout != 0)
            View.inflate(context, itemLayout, this)
        else
            View.inflate(context, R.layout.item_profile, this)

        editText = item_profile_et
        iconIV = item_profile_iv

        editText.hint = profileItemData.hint

        if (!profileItemData.isEditable) {
            editText.isEnabled = false
            editText.setTextColor(editText.hintTextColors)
        }

        setMode(profileItemData.type)

        profileItemData.drawable?.let {
            iconIV.visibility = View.VISIBLE
            iconIV.setImageResource(it)

            iconIV.setOnClickListener {
                profileItemData.onDrawableClicked.invoke(iconIV)
            }
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validate()

                changedListener.onChanged()
            }
        })
    }

    private fun setMode(mode: ProfileItemMode) {
        when (mode) {
            ProfileItemMode.TEXT -> {
                editText.inputType = profileItemData.inputType

                editText.setText(profileItemData.currentData)
            }
            ProfileItemMode.NOT_TEXT -> {
                editText.inputType = InputType.TYPE_NULL
                editText.setText(profileItemData.currentData)

                editText.setOnClickListener {
                    profileItemData.onClick.invoke(editText.text.toString(), editText)
                }
            }
            ProfileItemMode.SPINNER -> {
                editText.inputType = InputType.TYPE_NULL

                if (!profileItemData.dropDown.isNullOrEmpty()) {
                    if (profileItemData.dropDown.size < 2)
                        visibility = View.GONE
                    else {
                        setDropdown(profileItemData.dropDown, R.layout.item_dropdown) {
                            editText.setText(profileItemData.dropDown[it])
                        }
                    }

                    val current =
                        profileItemData.dropDown.find { item -> item == profileItemData.currentData }

                    editText.setText(current)

                } else {
                    Timber.w("profileItemData.dropDown is empty or null")
                }
            }
        }
    }

    override fun validate(): Boolean {
        val isOk = profileItemData.validation.invoke(editText.text.toString())

        val colorStateList = if (isOk) {
            ColorStateList.valueOf(profileItemData.lineColorDefault)
        } else {
            if (editText.text.toString().isNotEmpty())
                ColorStateList.valueOf(profileItemData.lineColorError)
            else
                ColorStateList.valueOf(profileItemData.lineColorDefault)
        }

        setBackgroundTintList(editText, colorStateList)

        return isOk
    }

    override fun getData(): String {
        return editText.text.toString()
    }

    override fun getItemData(): ProfileItemData {
        return profileItemData
    }

    private fun setDropdown(dataList: Array<String>, listItemId: Int, listener: (Int) -> Unit) {
        this.dataList = dataList
        listPopupView = ListPopupWindow(context)

        listPopupView.setAdapter(ArrayAdapter(context, listItemId, dataList) as ListAdapter?)
        listPopupView.anchorView = this

        editText.setOnClickListener { listPopupView.show() }

        listPopupView.setOnItemClickListener { _, _, position, _ ->
            editText.setText(dataList[position])

            listPopupView.dismiss()

            this.selected = position
            listener(position)
        }
    }

    fun setCurrentItem(position: Int) {
        dataList.let {
            if (position >= it.size) return
            this.selected = position

            editText.setText(dataList[position])
        }
    }
}