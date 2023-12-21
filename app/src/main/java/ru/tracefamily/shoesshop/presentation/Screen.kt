package ru.tracefamily.shoesshop.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProductInfoScreen(vm: MainViewModel = hiltViewModel()) {

    val state = vm.cardState.collectAsState()
    Column {
        Text(text = state.value.name)
        Button(onClick = { // ToDO
        }) {
            Text(text = "Click me")
        }
    }
}

@Composable
fun WarehouseScreen() {
    Text(text = "Warehouse")
}