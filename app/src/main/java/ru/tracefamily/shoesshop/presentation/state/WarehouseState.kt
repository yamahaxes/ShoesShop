package ru.tracefamily.shoesshop.presentation.state

import ru.tracefamily.shoesshop.domain.warehouse.model.Document

data class WarehouseState(
    val drafts: List<Document> = listOf()
)
