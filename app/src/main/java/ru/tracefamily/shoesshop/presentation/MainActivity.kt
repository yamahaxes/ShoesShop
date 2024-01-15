package ru.tracefamily.shoesshop.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExitTransition
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.tracefamily.shoesshop.R
import ru.tracefamily.shoesshop.presentation.screen.InfoScreen
import ru.tracefamily.shoesshop.presentation.screen.WarehouseScreen
import ru.tracefamily.shoesshop.presentation.theme.ShoesShopTheme
import ru.tracefamily.shoesshop.presentation.utils.Constants
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    private val vm: MainViewModel by viewModels()

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var scanner: GmsBarcodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.currentScreen = Constants.ProductInfoNavItem.route

        val moduleInstallClient = ModuleInstall.getClient(context)

        moduleInstallClient.areModulesAvailable(scanner)
            .addOnSuccessListener {
                if (!it.areModulesAvailable()) {

                    val moduleInstallRequest =
                        ModuleInstallRequest.newBuilder()
                            .addApi(scanner)
                            .build()

                    moduleInstallClient
                        .installModules(moduleInstallRequest)
                }
            }

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

        // subscribe viewmodel events
        val activityContext = this
        lifecycleScope.launch {
            vm.openDocumentState.collect { document ->
                if (document != null) {
                    val intent = Intent(activityContext, DocumentActivity::class.java)
                    intent.putExtra("id", document.id)
                    intent.putExtra("cell", document.cell)

                    activityContext.startActivity(
                        intent
                    )
                }
            }
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
                    label = { Text(text = stringResource(id = item.labelId)) },
                    alwaysShowLabel = false
                )
            }
        }

    }

    @Composable
    private fun BottomBarNavGraph(navController: NavHostController, paddingValues: PaddingValues) {

        NavHost(
            navController = navController,
            startDestination = Constants.ProductInfoNavItem.route,
            modifier = Modifier.padding(paddingValues),
            exitTransition = { ExitTransition.None },
            popExitTransition = { ExitTransition.None }
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
        val errorList = errorState.errors

        if (errorList.isNotEmpty()) {

            var errorAsString = ""
            errorList.forEach { error ->
                errorAsString =
                    errorAsString.plus(stringResource(id = error.resId)).plus(":\n")
                        .plus(error.message)
                        .plus("\n")
            }

            AlertDialog(
                onDismissRequest = { vm.confirmErrors() },
                confirmButton = { },
                title = { Text(text = stringResource(R.string.message_header_error)) },
                text = {
                    Text(
                        text = errorAsString
                    )
                }
            )
        }
    }
}