package ru.tracefamily.shoesshop.presentation.state

import ru.tracefamily.shoesshop.domain.info.model.Card
import ru.tracefamily.shoesshop.domain.info.model.CommonStocks
import ru.tracefamily.shoesshop.domain.info.model.Image
import ru.tracefamily.shoesshop.domain.info.model.Stocks

data class InfoState(
    val card: Card = Card(),
    val image: Image = Image(),
    val stocks: Stocks = Stocks(),
    val commonStocks: CommonStocks = CommonStocks()
)
