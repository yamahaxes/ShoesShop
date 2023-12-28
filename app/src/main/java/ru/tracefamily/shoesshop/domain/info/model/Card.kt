package ru.tracefamily.shoesshop.domain.info.model

import ru.tracefamily.shoesshop.domain.common.model.Barcode

data class Card(
    val name: String = "",
    val barcode: Barcode = Barcode(),
    val price: Int = 0,
    val priceBeforeDiscount: Int = 0
)
