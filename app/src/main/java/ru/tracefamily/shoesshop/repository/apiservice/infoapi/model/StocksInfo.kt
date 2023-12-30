package ru.tracefamily.shoesshop.repository.apiservice.infoapi.model

data class StocksInfo(
    val stock: List<StocksRowInfo>,
    val cells: List<StocksCellInfo>
)
