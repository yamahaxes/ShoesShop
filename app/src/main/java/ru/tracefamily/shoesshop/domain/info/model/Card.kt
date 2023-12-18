package ru.tracefamily.shoesshop.domain.info.model

data class Card(
    val name: String,
    val price: Int = 0,
    val priceBeforeDiscount: Int = 0
    )
