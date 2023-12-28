package ru.tracefamily.shoesshop.domain.repo

import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.info.model.Card
import ru.tracefamily.shoesshop.domain.info.model.CommonStocksRow
import ru.tracefamily.shoesshop.domain.info.model.Image
import ru.tracefamily.shoesshop.domain.info.model.Stocks

interface InfoRepo: Repo {

    suspend fun getCard(barcode: Barcode): Result<Card>
    suspend fun getImage(barcode: Barcode): Result<Image>
    suspend fun getStocks(barcode: Barcode): Result<Stocks>
    suspend fun getCommonStocks(barcode: Barcode): Result<List<CommonStocksRow>>

}