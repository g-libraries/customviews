package com.core.customviews.profile

import com.core.customviews.profile.view.ProfileItemData

interface ProfileItem {
    fun validate(): Boolean
    fun getData(): ProfileItemData
}