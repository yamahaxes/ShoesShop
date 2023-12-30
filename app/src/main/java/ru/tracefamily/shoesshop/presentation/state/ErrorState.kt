package ru.tracefamily.shoesshop.presentation.state

import androidx.annotation.StringRes

data class ErrorState (
    @StringRes val resId: Int,
    val description: String
)