package com.example.cashregister

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.cashregister.ui.theme.CashRegisterTheme
import com.example.cashregister.viewmodel.CashRegisterViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CashRegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CashRegisterTheme {

                var showHistory by remember {
                    mutableStateOf(false)
                }

                if (showHistory) {
                    HistoryScreen(
                        viewModel = viewModel,
                        onBackClick = {
                            showHistory = false
                        }
                    )
                } else {
                    MainScreen(
                        viewModel = viewModel,
                        onHistoryClick = {
                            showHistory = true
                        }
                    )
                }
            }
        }
    }
}