
package com.example.cashregister

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cashregister.model.Purchase

@Composable
fun PurchaseDetailsScreen(
    purchase: Purchase,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Purchase Details",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Purchase Number: ${purchase.id}")

        Spacer(modifier = Modifier.height(12.dp))

        Text("Purchase Date: ${purchase.date}")

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Products",
            style = MaterialTheme.typography.titleLarge
        )

        purchase.items.forEach { item ->
            Text("${item.product.name} x ${item.quantity}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Total: $${"%.2f".format(purchase.total)}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}
