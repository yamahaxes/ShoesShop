package ru.tracefamily.shoesshop.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ru.tracefamily.shoesshop.presentation.MainViewModel

@Composable
fun OpenDocument(vm: MainViewModel) {
    
    val document = vm.currentDocument
    
    Column {
        Text(text = document.cell)
        Text(text = "Descr: " + document.cell)
        Button(onClick = { vm.closeDocument() }) {
            Text(text = "Провести и закрыть")
        }
    }

    BackHandler {
        vm.closeDocument()
    }
}