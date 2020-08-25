package com.core.customviews.profile

import com.core.customviews.profile.view.ProfileEditModel
import com.core.customviews.profile.view.ProfileItemData


open class ProfileDataManager {

    lateinit var list: ArrayList<ProfileItem>
    lateinit var dataList: ArrayList<ProfileItemData>

    fun validate(): Boolean {
        for (profileItem in list) {
            if (!profileItem.validate())
                return false
        }

        return true
    }

    fun validateAndCompare(): Boolean {
        for ((index, item) in list.withIndex()) {
            if (item.getData() != dataList[index].currentData)
                return false
        }

        return true
    }

    fun getDataSet(): ArrayList<String?> {
        val dataSet = arrayListOf<String?>()

        for (item in list) {
            dataSet.add(item.getData())
        }

        return dataSet
    }
}