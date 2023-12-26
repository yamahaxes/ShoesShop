package ru.tracefamily.shoesshop.domain.info.model

data class Stocks(
    val rows: List<StocksRow>,
    val cells: List<StocksCell>
) {
    companion object {
        fun empty(): Stocks = Stocks(listOf(), listOf())
    }
}
