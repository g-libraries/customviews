package com.core.customviews.profile.view

interface IProfileDataLayout {
    fun setInitData(
        profileDataList: ArrayList<ProfileItemData>,
        profileEditMode: ProfileEditModel,
        listener: OnChangedListener
    )

    fun udpate(profileDataList: ArrayList<ProfileItemData>)
}

interface ValidateListener {
    fun onValidated(validate: Boolean)
}


interface OnChangedListener {
    fun onChanged()
}