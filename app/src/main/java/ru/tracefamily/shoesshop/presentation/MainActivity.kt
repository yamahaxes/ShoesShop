package ru.tracefamily.shoesshop.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.tracefamily.shoesshop.R
import ru.tracefamily.shoesshop.presentation.screen.InfoScreen
import ru.tracefamily.shoesshop.presentation.screen.WarehouseScreen
import ru.tracefamily.shoesshop.presentation.theme.ShoesShopTheme
import ru.tracefamily.shoesshop.presentation.utils.Constants

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.currentScreen = Constants.ProductInfoNavItem.route

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
    private fun BottomBar(navController: NavHostController) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        vm.currentScreen = currentDestination?.route ?: ""

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
    private fun BottomBarNavGraph(navController: NavHostController, paddingValues: PaddingValues) {

        NavHost(
            navController = navController,
            startDestination = Constants.ProductInfoNavItem.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Constants.ProductInfoNavItem.route) {
                InfoScreen(Modifier.fillMaxSize(), vm)
            }
            composable(route = Constants.WarehouseManagementNavItem.route) {
                WarehouseScreen(
                    Modifier.fillMaxWidth(),
                    vm
                )
            }
        }
    }

    @Composable
    private fun ScanBarcodeButton() {
        FloatingActionButton(
            onClick = { vm.scanBarcode() }
        ) {
            Icon(
                modifier = Modifier.padding(5.dp),
                painter = painterResource(id = R.drawable.ic_camera_float_button),
                contentDescription = null,
            )
        }
    }

    @Composable
    private fun ErrorDialog() {
        val errorState by vm.errorMessageState.collectAsState()

        if (errorState.resId != -1) {
            AlertDialog(
                onDismissRequest = { vm.confirmErrors() },
                confirmButton = { },
                title = { Text(text = stringResource(R.string.message_header_error)) },
                text = {
                    Text(
                        text = stringResource(id = errorState.resId) + if (errorState.description.isNotBlank()) {
                            ": ".plus(errorState.description)
                        } else { "" }
                    )
                }
            )
        }
    }
}