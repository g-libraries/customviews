package com.core.customviews.profile

interface ProfileItem {
    fun validate(): Boolean
    fun getData(): String?
}