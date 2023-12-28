package ru.tracefamily.shoesshop.domain.warehouse.usecase

import ru.tracefamily.shoesshop.domain.common.UseCaseExecutable
import ru.tracefamily.shoesshop.domain.warehouse.model.DocType
import ru.tracefamily.shoesshop.domain.warehouse.model.Document

class GetDraftsUseCase: UseCaseExecutable<DocType, List<Document>> {

    override suspend fun execute(input: DocType): Result<List<Document>> {
        TODO("Not yet implemented")
    }

}