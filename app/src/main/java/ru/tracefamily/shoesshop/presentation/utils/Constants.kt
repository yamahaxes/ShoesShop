package ru.tracefamily.shoesshop.presentation.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import ru.tracefamily.shoesshop.R
import ru.tracefamily.shoesshop.presentation.models.BottomNavItem

object Constants {

    val ProductInfoNavItem = BottomNavItem(
        route = "ProductInfo",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search,
        labelId = R.string.bottom_bar_label_product_info
    )

    val WarehouseManagementNavItem = BottomNavItem(
        route = "WarehouseManagement",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        labelId = R.string.bottom_bar_label_warehouse_management
    )

    val BottomNavItems = listOf(
        ProductInfoNavItem,
        WarehouseManagementNavItem,
    )

}