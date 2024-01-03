package ru.tracefamily.shoesshop.presentation.model

import androidx.annotation.StringRes
import androidx.core.graphics.drawable.IconCompat.IconType
import ru.tracefamily.shoesshop.domain.warehouse.model.DocType

data class FilterItem (
    @StringRes val textId: Int,
    @IconType val iconId: Int,
    val type: DocType
)