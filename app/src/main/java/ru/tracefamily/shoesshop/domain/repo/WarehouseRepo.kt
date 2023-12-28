package ru.tracefamily.shoesshop.domain.repo

import ru.tracefamily.shoesshop.domain.warehouse.model.Document

interface WarehouseRepo: Repo {

    fun getDrafts(): List<Document>

    fun postDocument(document: Document): Document

}