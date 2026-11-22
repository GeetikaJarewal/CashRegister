
package com.example.cashregister

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.cashregister.navigation.AppNavigation
import com.example.cashregister.ui.theme.CashRegisterTheme
import com.example.cashregister.viewmodel.CashRegisterViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CashRegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CashRegisterTheme {
                AppNavigation(
                    viewModel = viewModel
                )
            }
        }
    }
}
