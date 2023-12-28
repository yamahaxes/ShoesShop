package ru.tracefamily.shoesshop.domain.repo

import ru.tracefamily.shoesshop.domain.common.model.ConnectSettings

interface Repo {
    val connectSettings: ConnectSettings
}