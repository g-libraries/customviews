package com.core.customviews.profile

import com.core.customviews.profile.view.ProfileDataLayout
import com.core.customviews.profile.view.ProfileEditModel
import com.core.customviews.profile.view.ProfileItemData


abstract class ProfileDataManager {

    var list: ArrayList<ProfileItem> = arrayListOf()
    var profileItemDataList: ArrayList<ProfileItemData> = arrayListOf()
    var dataList: ArrayList<String?> = arrayListOf()
    
    fun setLayout(list: ArrayList<ProfileItem>) {
        this.list = list
        dataList = getDataSet()
    }

    fun validate(): Boolean {
        for (profileItem in list) {
            if (!profileItem.validate())
                return false
        }

        return true
    }

    fun validateAndCompare(): Boolean {
        var changed = false
        var validated = true

        for ((index, item) in list.withIndex()) {
            if (!item.validate())
                validated = false

            if (item.getData() != dataList[index])
                changed = true
        }

        return validated && changed
    }

    fun getProfileItemDataSet(): ArrayList<ProfileItemData?> {
        val dataSet = arrayListOf<ProfileItemData?>()

        for (item in list) {
            dataSet.add(item.getItemData())
        }

        return dataSet
    }

    fun getDataPairSet(): ArrayList<Pair<Int, String?>> {
        val dataSet = arrayListOf<Pair<Int, String?>>()

        for (item in list) {
            dataSet.add(Pair(item.getItemData().dataType, item.getData()))
        }

        return dataSet
    }

    fun getDataSet(): ArrayList<String?> {
        val dataSet = arrayListOf<String?>()

        for (item in list) {
            dataSet.add(item.getData())
        }

        return dataSet
    }
}