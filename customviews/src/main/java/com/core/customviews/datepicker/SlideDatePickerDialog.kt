package com.core.customviews.datepicker

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.core.customviews.R
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


class SlideDatePickerDialog private constructor(
    private var startDate: Calendar = Calendar.getInstance().apply {
        this.set(Calendar.YEAR, this.get(Calendar.YEAR) - 100)
        this.set(Calendar.MONTH, 0)
        this.set(Calendar.DAY_OF_MONTH, 1)
    },
    private var endDate: Calendar = Calendar.getInstance(),
    private var preselectedDate: Calendar = Calendar.getInstance(),
    private var yearModifier: Int = 0,
    private var locale: Locale = Locale.US,
    private var themeColor: Int = -1,
    private var headerTextColor: Int = -1,
    private var headerDateFormat: String = "EEE, MMM dd",
    private var showYear: Boolean = true,
    private var cancelText: String = "",
    private var confirmText: String = "",
    private var callback: SlideDatePickerDialogCallback? = null
) :
    DialogFragment() {

    private lateinit var viewModel: SlideDatePickerDialogViewModel

    private lateinit var rootView: View
    private val topContainer: LinearLayout by lazy { rootView.findViewById<LinearLayout>(R.id.top_container) }
    private val tvYear: TextView by lazy { rootView.findViewById<TextView>(R.id.tv_year) }
    private val tvDate: TextView by lazy { rootView.findViewById<TextView>(R.id.tv_date) }
    private val recyclerViewDay: RecyclerView by lazy { rootView.findViewById<RecyclerView>(R.id.recycler_view_day) }
    private val recyclerViewMonth: RecyclerView by lazy { rootView.findViewById<RecyclerView>(R.id.recycler_view_month) }
    private val recyclerViewYear: RecyclerView by lazy { rootView.findViewById<RecyclerView>(R.id.recycler_view_year) }
    private val btnCancel: Button by lazy { rootView.findViewById<Button>(R.id.btn_cancel) }
    private val btnConfirm: Button by lazy { rootView.findViewById<Button>(R.id.btn_confirm) }

    private val dayAdapter = SlideAdapter(SlideAdapter.Type.DAY)
    private val monthAdapter = SlideAdapter(SlideAdapter.Type.MONTH)
    private val yearAdapter = SlideAdapter(SlideAdapter.Type.YEAR)
    private val daySnapHelper = LinearSnapHelper()
    private val monthSnapHelper = LinearSnapHelper()
    private val yearSnapHelper = LinearSnapHelper()
    lateinit var dayLayoutManager: LinearLayoutManager
    lateinit var monthLayoutManager: LinearLayoutManager
    lateinit var yearLayoutManager: LinearLayoutManager

    private var yearLastScrollState = RecyclerView.SCROLL_STATE_SETTLING
    private var monthLastScrollState = RecyclerView.SCROLL_STATE_SETTLING
    private var dayLastScrollState = RecyclerView.SCROLL_STATE_SETTLING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.BaseDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.dialog_slide_date_picker, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SlideDatePickerDialogViewModel(startDate, endDate, preselectedDate) as T
            }

        }).get(SlideDatePickerDialogViewModel::class.java)
        initialize()
        observe()
    }

    private fun initialize() {
        dayLayoutManager = LinearLayoutManager(context)
        monthLayoutManager = LinearLayoutManager(context)
        yearLayoutManager = LinearLayoutManager(context)

        recyclerViewDay.layoutManager = dayLayoutManager
        recyclerViewDay.adapter = dayAdapter
        recyclerViewDay.addOnScrollListener(dayScrollListener)
        recyclerViewDay.onFlingListener = null
        daySnapHelper.attachToRecyclerView(recyclerViewDay)

        recyclerViewMonth.layoutManager = monthLayoutManager
        recyclerViewMonth.adapter = monthAdapter
        recyclerViewMonth.addOnScrollListener(monthScrollListener)
        recyclerViewMonth.onFlingListener = null
        monthSnapHelper.attachToRecyclerView(recyclerViewMonth)

        recyclerViewYear.layoutManager = yearLayoutManager
        recyclerViewYear.adapter = yearAdapter
        recyclerViewYear.addOnScrollListener(yearScrollListener)
        recyclerViewYear.onFlingListener = null
        yearSnapHelper.attachToRecyclerView(recyclerViewYear)

        btnConfirm.setOnClickListener {
            onDialogConfirm()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        setConfiguration()
    }

    private fun setConfiguration() {

        if (themeColor != -1) {
            topContainer.setBackgroundColor(themeColor)
            btnConfirm.setTextColor(themeColor)
            btnCancel.setTextColor(themeColor)
        }

        if (!showYear) {
            tvYear.visibility = View.GONE
        }

        if (headerDateFormat.isEmpty()) {
            headerDateFormat = "EEE, MMM dd"
        }

        if (cancelText.isNotBlank()) {
            btnCancel.text = cancelText
        }

        if (confirmText.isNotBlank()) {
            btnConfirm.text = confirmText
        }

        monthAdapter.locale = locale
        yearAdapter.yearModifier = yearModifier
    }

    private fun onDialogConfirm() {
        val day = viewModel.getCurrentDay().value!!
        val month = viewModel.getCurrentMonth().value!!
        val year = viewModel.getCurrentYear().value!!
        val calendar = viewModel.getCalendar().value!!
        callback?.onPositiveClick(day, month, year, calendar)
        dismiss()
    }

    private fun observe() {
        viewModel.getDays().observe(this, Observer {
            dayAdapter.data = it
            dayAdapter.notifyDataSetChanged()
            viewModel.getCurrentDay().value?.let { day ->
                val currentDayPosition = dayAdapter.getPositionByValue(day)
                recyclerViewDay.alpha = 0f
                recyclerViewDay.animate().alpha(1f).apply {
                    duration = 200
                }.start()
                recyclerViewDay.scrollToPosition(currentDayPosition)
                recyclerViewDay.smoothScrollToPosition(currentDayPosition)
            }
        })

        viewModel.getMonths().observe(this, Observer {
            monthAdapter.data = it
            monthAdapter.notifyDataSetChanged()
            viewModel.getCurrentMonth().value?.let { month ->
                val currentMonthPosition = monthAdapter.getPositionByValue(month)
                recyclerViewMonth.alpha = 0f
                recyclerViewMonth.animate().alpha(1f).apply {
                    duration = 200
                }.start()
                recyclerViewMonth.scrollToPosition(currentMonthPosition)
                recyclerViewMonth.smoothScrollToPosition(currentMonthPosition)
            }
        })

        viewModel.getYears().observe(this, Observer {
            yearAdapter.data = it
            yearAdapter.notifyDataSetChanged()
            viewModel.getCurrentYear().value?.let { year ->
                val currentYearPosition = yearAdapter.getPositionByValue(year)
                recyclerViewYear.scrollToPosition(currentYearPosition)
                recyclerViewYear.smoothScrollToPosition(currentYearPosition)
            }
        })

        viewModel.getCalendar().observe(this, Observer {
            tvDate.text = SimpleDateFormat(headerDateFormat, locale).format(it.time)
        })

        viewModel.getCurrentYear().observe(this, Observer {
            tvYear.text = (it + yearModifier).toString()
        })
    }

    private val dayScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            if (newState == RecyclerView.SCROLL_STATE_IDLE && dayLastScrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                val snappedView = daySnapHelper.findSnapView(dayLayoutManager)
                snappedView?.let { view ->
                    val day = dayAdapter.getValueByPosition(dayLayoutManager.getPosition(view))
                    viewModel.setDay(day)
                }
            }

            dayLastScrollState = newState
        }
    }

    private val monthScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            if (newState == RecyclerView.SCROLL_STATE_IDLE && monthLastScrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                val snappedView = monthSnapHelper.findSnapView(monthLayoutManager)
                snappedView?.let { view ->
                    val month =
                        monthAdapter.getValueByPosition(monthLayoutManager.getPosition(view))
                    viewModel.setMonth(month)
                }
            }

            monthLastScrollState = newState
        }
    }

    private val yearScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            if (newState == RecyclerView.SCROLL_STATE_IDLE && yearLastScrollState == RecyclerView.SCROLL_STATE_SETTLING) {
                val snappedView = yearSnapHelper.findSnapView(yearLayoutManager)
                snappedView?.let { view ->
                    val year = yearAdapter.getValueByPosition(yearLayoutManager.getPosition(view))
                    viewModel.setYear(year)
                }
            }

            yearLastScrollState = newState
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerViewDay.removeOnScrollListener(dayScrollListener)
        recyclerViewMonth.removeOnScrollListener(monthScrollListener)
        recyclerViewYear.removeOnScrollListener(yearScrollListener)
    }

    class Builder() {
        private var startDate: Calendar = Calendar.getInstance().apply {
            this.set(Calendar.YEAR, this.get(Calendar.YEAR) - 100)
            this.set(Calendar.MONTH, 0)
            this.set(Calendar.DAY_OF_MONTH, 1)
        }
        private var endDate: Calendar = Calendar.getInstance()
        private var preselectedDate: Calendar = Calendar.getInstance()
        private var yearModifier: Int = 0
        private var locale: Locale = Locale.US
        @ColorInt
        private var themeColor: Int = -1
        @ColorInt
        private var headerTextColor: Int = -1
        private var itemTextColor: Int = -1
        private var itemTextBg: Int = -1
        private var headerDateFormat: String = "EEE, MMM dd"
        private var showYear = true
        private var cancelText = ""
        private var confirmText = ""
        private var callback: SlideDatePickerDialogCallback? = null

        fun setItemTextColor(itemTextColor: Int): Builder = this.apply {
            this.itemTextColor = itemTextColor
        }

        fun setItemTextBg(itemTextBg: Int): Builder = this.apply {
            this.itemTextBg = itemTextBg
        }

        fun setStartDate(startDate: Calendar): Builder = this.apply {
            this.startDate = startDate
        }

        fun setEndDate(endDate: Calendar): Builder = this.apply {
            this.endDate = endDate
        }

        fun setPreselectedDate(preselectedDate: Calendar): Builder = this.apply {
            this.preselectedDate = preselectedDate
        }

        fun setYearModifier(yearModifier: Int): Builder = this.apply {
            this.yearModifier = yearModifier
        }

        fun setLocale(locale: Locale): Builder = this.apply {
            this.locale = locale
        }

        fun setCallback(callback: SlideDatePickerDialogCallback): Builder = this.apply {
            this.callback = callback
        }

        fun setThemeColor(@ColorInt themeColor: Int): Builder = this.apply {
            this.themeColor = themeColor
        }

        fun setHeaderTextColor(@ColorRes headerTextColor: Int): Builder = this.apply {
            this.headerTextColor = headerTextColor
        }

        fun setHeaderDateFormat(headerDateFormat: String): Builder = this.apply {
            this.headerDateFormat = headerDateFormat
        }

        fun setShowYear(showYear: Boolean): Builder = this.apply {
            this.showYear = showYear
        }

        fun setCancelText(cancelText: String): Builder = this.apply {
            this.cancelText = cancelText
        }

        fun setConfirmText(confirmText: String): Builder = this.apply {
            this.confirmText = confirmText
        }

        fun build(): SlideDatePickerDialog = SlideDatePickerDialog(
            startDate,
            endDate,
            preselectedDate,
            yearModifier,
            locale,
            themeColor,
            headerTextColor,
            headerDateFormat,
            showYear,
            cancelText,
            confirmText,
            callback
        )
    }

}


interface SlideDatePickerDialogCallback : Parcelable, Serializable {
    fun onPositiveClick(day: Int, month: Int, year: Int, calendar: Calendar)
}