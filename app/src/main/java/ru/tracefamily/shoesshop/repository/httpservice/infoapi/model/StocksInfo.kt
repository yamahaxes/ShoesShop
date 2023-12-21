package ru.tracefamily.shoesshop.repository.httpservice.infoapi.model

data class StocksInfo(
    val rows: List<StocksRowInfo>,
    val cells: List<StocksCellInfo>
)
