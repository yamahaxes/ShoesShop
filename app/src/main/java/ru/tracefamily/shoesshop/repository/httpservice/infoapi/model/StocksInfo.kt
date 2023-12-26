package ru.tracefamily.shoesshop.repository.httpservice.infoapi.model

data class StocksInfo(
    val stock: List<StocksRowInfo>,
    val cells: List<StocksCellInfo>
)
