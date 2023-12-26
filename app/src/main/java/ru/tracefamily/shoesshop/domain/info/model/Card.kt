package ru.tracefamily.shoesshop.domain.info.model

import ru.tracefamily.shoesshop.domain.common.model.Barcode

data class Card(
    val name: String,
    val barcode: Barcode,
    val price: Int,
    val priceBeforeDiscount: Int
    ) {
    companion object {
        fun empty(): Card = Card("", Barcode(""), 0, 0)
    }
}
