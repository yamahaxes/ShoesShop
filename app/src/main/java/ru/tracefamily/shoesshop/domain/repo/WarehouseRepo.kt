package ru.tracefamily.shoesshop.domain.repo

import ru.tracefamily.shoesshop.domain.warehouse.model.Document

interface WarehouseRepo {

    suspend fun getDrafts(): List<Document>

    suspend fun postDocument(document: Document): Document

}