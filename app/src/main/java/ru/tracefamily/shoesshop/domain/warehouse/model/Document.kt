package ru.tracefamily.shoesshop.domain.warehouse.model

data class Document(
    val type: DocumentType,
    val cell: String,
    val description: String,
    val composition: List<CompositionRow>
)

enum class DocumentType {
    ADD,
    DELETE,
    INVENTORY
}
