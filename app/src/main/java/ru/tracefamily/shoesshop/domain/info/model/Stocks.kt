package ru.tracefamily.shoesshop.domain.info.model

data class Stocks(
    val rows: List<StocksRow> = listOf(),
    val cells: List<StocksCell> = listOf()
)
