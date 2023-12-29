package ru.tracefamily.shoesshop.domain.warehouse.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import ru.tracefamily.shoesshop.domain.common.UseCaseExecutable
import ru.tracefamily.shoesshop.domain.repo.WarehouseRepo
import ru.tracefamily.shoesshop.domain.warehouse.model.Document
import javax.inject.Inject

@ViewModelScoped
class PostDocumentUseCase @Inject constructor(
    private val apiRepo: WarehouseRepo
) : UseCaseExecutable<Document, Document> {

    override suspend fun execute(input: Document): Result<Document> =
        try {
            Result.success(apiRepo.postDocument(input))
        } catch (e: Throwable) {
            Result.failure(e)
        }
}