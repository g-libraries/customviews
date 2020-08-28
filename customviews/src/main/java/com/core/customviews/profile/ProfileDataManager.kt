package com.core.customviews.profile

import com.core.customviews.profile.view.ProfileEditModel
import com.core.customviews.profile.view.ProfileItemData


abstract class ProfileDataManager {

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
            if (item.getData().currentData != dataList[index].currentData)
                return false
        }

        return true
    }

    fun getDataSet(): ArrayList<ProfileItemData?> {
        val dataSet = arrayListOf<ProfileItemData?>()

        for (item in list) {
            dataSet.add(item.getData())
        }

        return dataSet
    }

}