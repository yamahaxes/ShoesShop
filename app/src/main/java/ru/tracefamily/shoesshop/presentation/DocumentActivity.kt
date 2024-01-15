package ru.tracefamily.shoesshop.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import ru.tracefamily.shoesshop.presentation.theme.ShoesShopTheme

@AndroidEntryPoint
class DocumentActivity : ComponentActivity() {

    private val vm: DocumentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id: Int = intent.extras?.getInt("id") ?: -1
        val cellName = intent.extras?.getString("cell") ?: ""


        setContent {
            ShoesShopTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text(text = cellName)
                    Text(text = id.toString())
                }
            }
        }
    }
}