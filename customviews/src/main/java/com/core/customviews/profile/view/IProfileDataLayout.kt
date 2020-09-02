package com.core.customviews.profile.view

import com.core.customviews.profile.ProfileItem

interface IProfileDataLayout {
    fun setInitData(
        profileDataList: ArrayList<ProfileItemData>,
        profileEditMode: ProfileEditModel,
        listener: OnChangedListener
    )

    fun udpate(profileDataList: ArrayList<ProfileItemData>)

    fun getItems(): ArrayList<ProfileItem>
}

interface ValidateListener {
    fun onValidated(validate: Boolean)
}


interface OnChangedListener {
    fun onChanged()
}