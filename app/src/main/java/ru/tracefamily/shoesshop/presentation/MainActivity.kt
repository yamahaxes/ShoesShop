package ru.tracefamily.shoesshop.presentation

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.tracefamily.shoesshop.presentation.theme.ShoesShopTheme
import ru.tracefamily.shoesshop.presentation.utils.Constants

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoesShopTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    Scaffold(
                        bottomBar = { BottomBar(navController) },
                        floatingActionButton = { ScanBarcodeButton() },
                        floatingActionButtonPosition = FabPosition.Center
                    ) { paddingValues ->
                        BottomBarNavGraph(navController = navController, paddingValues)
                    }
                }
            }
            ErrorDialog()
        }
    }

    @Composable
    fun BottomBar(navController: NavHostController) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        NavigationBar {
            Constants.BottomNavItems.forEach { item ->
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentDestination?.hierarchy?.any { it.route == item.route } == true)
                                item.selectedIcon
                            else
                                item.unselectedIcon,
                            contentDescription = item.route
                        )
                    },
                    label = { Text(text = stringResource(id = item.labelId)) })
            }
        }

    }

    @Composable
    fun BottomBarNavGraph(navController: NavHostController, paddingValues: PaddingValues) {

        NavHost(
            navController = navController,
            startDestination = Constants.ProductInfoNavItem.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Constants.ProductInfoNavItem.route) {
                ProductInfoScreen()
            }
            composable(route = Constants.WarehouseManagementNavItem.route) {
                WarehouseScreen()
            }
        }
    }

    @Composable
    fun ScanBarcodeButton() {
        FloatingActionButton(onClick = { vm.scanBarcode() }) {
            Text(text = "Scan")
        }
    }

    @Composable
    fun ProductInfoScreen() {
        Column {
            ImageBlock()
            CardBlock()
        }
    }

    @Composable
    fun ImageBlock() {
        val state = vm.imageState.collectAsState()

        Card {
            if (state.value.base64Value.isNotBlank()) {
                val decodedString: ByteArray =
                    Base64.decode(state.value.base64Value, Base64.DEFAULT)
                val decodedByte =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                Image(
                    bitmap = decodedByte.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight
                )
            }
        }
    }

    @Composable
    fun CardBlock() {
        val state = vm.cardState.collectAsState()

        Card {
            Text(text = state.value.name)
            Text(text = state.value.price.toString())
            Text(text = state.value.priceBeforeDiscount.toString())
        }
    }

    @Composable
    fun WarehouseScreen() {
        Text(text = "Warehouse")
    }

    @Composable
    fun ErrorDialog() {
        val errorState by vm.errorMessageState.collectAsState()

        if (errorState.message.isNotBlank()) {
            AlertDialog(
                onDismissRequest = { vm.confirmErrors() },
                confirmButton = { },
                title = { Text(text = "Error") },
                text = { Text(text = errorState.message) }
            )
        }
    }
}