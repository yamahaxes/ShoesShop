package ru.tracefamily.shoesshop.presentation.state

import ru.tracefamily.shoesshop.domain.common.model.Error

data class ErrorState(
    val errors: MutableList<Error>
)
