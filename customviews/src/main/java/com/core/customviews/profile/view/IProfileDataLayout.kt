package com.core.customviews.profile.view

interface IProfileDataLayout {
    fun setInitData(profileDataList: ArrayList<ProfileItemData>, profileEditMode: ProfileEditModel,listener: ValidateListener)
}

interface ValidateListener {
    fun onValidated(validate: Boolean)
}