
package com.example.cashregister

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cashregister.model.Purchase
import com.example.cashregister.viewmodel.CashRegisterViewModel

@Composable
fun HistoryScreen(
    viewModel: CashRegisterViewModel,
    onBackClick: () -> Unit
) {

    // Observe purchase history
    val purchases by viewModel.purchaseHistory.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Screen heading
        Text(
            text = "Purchase History",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Check whether purchase history is empty
        if (purchases.isEmpty()) {

            Text("No purchases yet")

        } else {

            // Display all completed purchases
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(purchases) { purchase ->
                    PurchaseCard(purchase)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Return to main screen
        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Cash Register")
        }
    }
}


// Individual purchase card
@Composable
fun PurchaseCard(purchase: Purchase) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                text = "Purchase #${purchase.id}",
                style = MaterialTheme.typography.titleMedium
            )

            Text("Date: ${purchase.date}")

            // Display products included in this purchase
            purchase.items.forEach { item ->

                Text(
                    "${item.product.name} x ${item.quantity}"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total: $${"%.2f".format(purchase.total)}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
