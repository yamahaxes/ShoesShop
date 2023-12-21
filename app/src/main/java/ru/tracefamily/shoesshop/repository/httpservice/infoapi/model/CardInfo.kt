package ru.tracefamily.shoesshop.repository.httpservice.infoapi.model

data class CardInfo(
    val name: String,
    val price: Int,
    val priceBeforeDiscount: Int
)
