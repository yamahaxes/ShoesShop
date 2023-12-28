package ru.tracefamily.shoesshop.domain.warehouse.usecase

import ru.tracefamily.shoesshop.domain.common.UseCaseExecutable
import ru.tracefamily.shoesshop.domain.warehouse.model.Document

class PostDocumentUseCase: UseCaseExecutable<Document, Document> {

    override suspend fun execute(input: Document): Result<Document> {
        TODO("Not yet implemented")
    }

}