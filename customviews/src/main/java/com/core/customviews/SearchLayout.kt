package com.core.customviews

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.core.base.usecases.setOnOneClickListener
import kotlinx.android.synthetic.main.search_view.view.*

class SearchLayout : ConstraintLayout, View.OnFocusChangeListener {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

//    constructor(
//        context: Context?,
//        attrs: AttributeSet,
//        defStyleAttr: Int,
//        defStyleRes: Int
//    ) : super(context, attrs, defStyleAttr, defStyleRes) {
//        init(attrs)
//    }

    private lateinit var searchET: AppCompatEditText
    private lateinit var searchIV: AppCompatImageView
    private lateinit var clearIV: AppCompatImageView

    lateinit var textWatcher: TextWatcher

    private var searchIconInactive: Int = 0
    private var searchIconActive: Int = 0
    private var searchIconClear: Int = 0
    private var backgroundDrawable: Int = 0

    private var searchHintColor: Int = 0
    private var searchTextColor: Int = 0

    private var searchTextSize: Float = 0f

    private var layoutBackround: Int = 0

    private var deleteUnderLine: Boolean = true

    private var hintText: String = ""

    private fun init(attrs: AttributeSet) {
        View.inflate(context, R.layout.search_view, this)

        val array = context.obtainStyledAttributes(attrs, R.styleable.SearchLayout)

        searchIconActive = array.getResourceId(
            R.styleable.SearchLayout_sl_icon_searchActive,
            R.drawable.ic_search_active
        )

        searchIconInactive = array.getResourceId(
            R.styleable.SearchLayout_sl_icon_searchInactive,
            R.drawable.ic_search_inactive
        )

        searchTextSize = array.getDimension(
            R.styleable.SearchLayout_sl_textSize,
            context.resources.getDimension(R.dimen._9sdp)
        )

        searchIconClear = array.getResourceId(
            R.styleable.SearchLayout_sl_icon_clearSearch,
            R.drawable.ic_clear_search
        )

        backgroundDrawable = array.getResourceId(
            R.styleable.SearchLayout_sl_background,
            R.drawable.search_rounded_et_bg
        )

        searchHintColor = array.getColor(
            R.styleable.SearchLayout_sl_textColorHint, ContextCompat.getColor(
                context, R.color.searchHint
            )
        )

        searchTextColor = array.getColor(
            R.styleable.SearchLayout_sl_textColor, ContextCompat.getColor(
                context, R.color.searchAccent
            )
        )

        deleteUnderLine = array.getBoolean(
            R.styleable.SearchLayout_sl_deleteUnderline, true
        )

        hintText = array.getString(R.styleable.SearchLayout_sl_hint)!!

        array.recycle()

        searchIV = search_view_search_iv
        searchET = search_view_input_et
        clearIV = search_view_clear_iv

        searchIV.setOnOneClickListener {
            searchET.requestFocus()
        }

        if (deleteUnderLine)
            searchET.setBackgroundColor(Color.TRANSPARENT)

        background = context.getDrawable(backgroundDrawable)

        searchET.onFocusChangeListener = this
        searchET.hint = hintText
        searchET.setTextColor(searchTextColor)
        searchET.setHintTextColor(searchHintColor)
        searchET.setTextSize(TypedValue.COMPLEX_UNIT_PX, searchTextSize)

        searchET.addTextChangedListener(insideTextWatcher)

        clearIV.setOnClickListener(onClearClickListener)

        deactivate()
        hideClear()
    }

    fun attachTextWather(textWatcher: TextWatcher) {
        searchET.addTextChangedListener(textWatcher)
    }

    fun setHint(hint: String) {
        searchET.hint = hint
    }

    private val insideTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!s.isNullOrEmpty()) {
                showClear()
            } else {
                hideClear()
            }

        }
    }

    private val onClearClickListener = OnClickListener { searchET.text?.clear() }

    private fun activate() {
        searchIV.setImageResource(searchIconActive)
    }

    private fun deactivate() {
        searchIV.setImageResource(searchIconInactive)
    }

    private fun showClear() {
        clearIV.visibility = View.VISIBLE
        requestLayout()
    }

    private fun hideClear() {
        clearIV.visibility = View.INVISIBLE
    }

    fun getInputView(): View {
        return searchET
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (hasFocus) {
            activate()
            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
        } else {
            deactivate()
            imm.hideSoftInputFromWindow(v?.windowToken, 0)
        }
    }
}