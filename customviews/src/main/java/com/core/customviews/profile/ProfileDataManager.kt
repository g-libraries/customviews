package com.core.customviews.profile

import com.core.customviews.profile.view.ProfileEditModel
import com.core.customviews.profile.view.ProfileItemData


abstract class ProfileDataManager {

    lateinit var list: ArrayList<ProfileItem>
    lateinit var profileItemDataList: ArrayList<ProfileItemData>
    lateinit var dataList: ArrayList<String?>

    fun validate(): Boolean {
        for (profileItem in list) {
            if (!profileItem.validate())
                return false
        }

        return true
    }

    fun validateAndCompare(): Boolean {
        for ((index, item) in list.withIndex()) {
            if (!item.validate() || item.getData() == dataList[index])
                return false
        }

        return true
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