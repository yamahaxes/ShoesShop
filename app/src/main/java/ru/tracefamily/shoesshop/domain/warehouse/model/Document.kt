package ru.tracefamily.shoesshop.domain.warehouse.model

data class Document(
    val type: DocType,
    val cell: String,
    val description: String,
    val content: List<ContentRow>
)