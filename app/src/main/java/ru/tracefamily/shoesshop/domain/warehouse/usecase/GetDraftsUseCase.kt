package ru.tracefamily.shoesshop.domain.warehouse.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import ru.tracefamily.shoesshop.domain.common.UseCaseExecutable
import ru.tracefamily.shoesshop.domain.repo.WarehouseRepo
import ru.tracefamily.shoesshop.domain.warehouse.model.DocType
import ru.tracefamily.shoesshop.domain.warehouse.model.Document
import javax.inject.Inject

@ViewModelScoped
class GetDraftsUseCase @Inject constructor(
    private val apiRepo: WarehouseRepo
) : UseCaseExecutable<DocType, List<Document>> {

    override suspend fun execute(input: DocType): Result<List<Document>> =
        try {
            Result.success(apiRepo.getDrafts().filter { it.type == input })
        } catch (e: Throwable) {
            Result.failure(e)
        }
}