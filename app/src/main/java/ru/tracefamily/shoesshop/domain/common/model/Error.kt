package ru.tracefamily.shoesshop.domain.common.model

import androidx.annotation.StringRes

data class Error(
    @StringRes val resId: Int = -1,
    val message: String = ""
)
