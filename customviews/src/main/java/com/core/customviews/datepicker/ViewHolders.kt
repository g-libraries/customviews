package com.core.customviews.datepicker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.core.customviews.R
import java.util.*

class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(month: Int, locale: Locale = Locale.US) {
        val textView = itemView.findViewById<TextView>(R.id.tv_month)
        val monthName = getMonths(locale)[month - 1]
        textView.text = monthName
    }
}

class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(day: Int) {
        val textView = itemView.findViewById<TextView>(R.id.tv_day)
        textView.text = format2LengthDay(day)
    }
}

class YearViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(year: Int, yearModifier: Int = 0) {
        val textView = itemView.findViewById<TextView>(R.id.tv_year)
        textView.text = (year + yearModifier).toString()
    }
}