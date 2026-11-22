
package com.example.cashregister.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cashregister.ManagerScreen
import com.example.cashregister.HistoryScreen
import com.example.cashregister.viewmodel.CashRegisterViewModel
import com.example.cashregister.CashierScreen
import com.example.cashregister.RestockScreen
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.cashregister.model.Purchase
import com.example.cashregister.PurchaseDetailsScreen

@Composable
fun AppNavigation(
    viewModel: CashRegisterViewModel
) {
    // Creates the navigation controller
    val navController = rememberNavController()

    val selectedPurchase = remember {
        mutableStateOf<Purchase?>(null)
    }

    // Defines the screens in our application
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // 1. Cashier Home Screen
        composable("home") {
            CashierScreen(
                viewModel = viewModel,
                onManagerClick = {
                    navController.navigate("manager")
                }
            )
        }

        // 2. Manager Panel
        composable("manager") {
            ManagerScreen(
                onHistoryClick = {
                    navController.navigate("history")
                },
                onRestockClick = {
                    navController.navigate("restock")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 3. Purchase History
        composable("history") {
            HistoryScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                onPurchaseClick = { purchase ->
                    selectedPurchase.value = purchase
                    navController.navigate("details")
                }
            )
        }

        composable("details") {
            val purchase = selectedPurchase.value

            if (purchase != null) {
                PurchaseDetailsScreen(
                    purchase = purchase,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }

        // 4. Temporary Restock Screen
        composable("restock") {
            RestockScreen(
                viewModel = viewModel,
                onCancelClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
