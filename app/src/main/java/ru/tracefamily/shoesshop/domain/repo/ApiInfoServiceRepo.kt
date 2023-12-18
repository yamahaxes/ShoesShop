package ru.tracefamily.shoesshop.domain.repo

import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.info.model.Card
import ru.tracefamily.shoesshop.domain.info.model.CommonStocksRow
import ru.tracefamily.shoesshop.domain.info.model.Image
import ru.tracefamily.shoesshop.domain.info.model.Stocks

interface ApiInfoServiceRepo {

    fun getCard(barcode: Barcode): Result<Card>
    fun getImage(barcode: Barcode): Result<Image>
    fun getStocks(barcode: Barcode): Result<Stocks>
    fun getCommonStocks(barcode: Barcode): Result<List<CommonStocksRow>>

}