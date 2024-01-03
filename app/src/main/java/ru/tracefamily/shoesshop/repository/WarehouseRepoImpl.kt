package ru.tracefamily.shoesshop.repository

import dagger.hilt.android.scopes.ServiceScoped
import ru.tracefamily.shoesshop.domain.repo.WarehouseRepo
import ru.tracefamily.shoesshop.domain.warehouse.model.DocType
import ru.tracefamily.shoesshop.domain.warehouse.model.Document
import ru.tracefamily.shoesshop.repository.model.ConnectSettings

@ServiceScoped
class WarehouseRepoImpl(
    private val connectSettings: ConnectSettings
) : WarehouseRepo {
    override suspend fun getDrafts(): List<Document> {
        // fake data
        return listOf(
            Document(id = 1, type = DocType.ADD, "A-1-1", "0000001", listOf()),
            Document(id = 2, type = DocType.WITHDRAW, "A-1-2", "0000002", listOf()),
            Document(id = 3, type = DocType.RECALCULATE, "A-2-1", "0000003", listOf())
        )
    }

    override suspend fun postDocument(document: Document): Document {
        // TODO
        return document
    }
}