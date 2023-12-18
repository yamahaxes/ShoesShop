package ru.tracefamily.shoesshop.domain.info.model

data class Stocks(
    val rows: List<StocksRow>,
    val cells: List<StocksCell>
)
