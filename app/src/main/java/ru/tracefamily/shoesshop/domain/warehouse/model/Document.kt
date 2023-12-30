package ru.tracefamily.shoesshop.domain.warehouse.model

data class Document(
    var id: Int = -1,
    val type: DocType,
    val cell: String,
    val description: String,
    val content: List<ContentRow>
)