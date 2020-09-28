package com.core.customviews.profile

import androidx.core.util.PatternsCompat

class Validator {
    val validateNameFun: (String?) -> Boolean = { name ->
        if (!name.isNullOrEmpty()) {
            name.trim().length > 2
        } else
            false
    }

    val validateEmailFun: (String?) -> Boolean = { email ->
        if (!email.isNullOrEmpty()) {
            email.trim()

            PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
        } else
            false
    }
}