package ru.tracefamily.shoesshop.domain.warehouse.model

import ru.tracefamily.shoesshop.domain.common.model.Barcode

data class ContentRow(
    val barcode: Barcode,
    val kiz: Barcode
)
