
package com.example.cashregister

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cashregister.viewmodel.CashRegisterViewModel

@Composable
fun CashierScreen(
    viewModel: CashRegisterViewModel,
    onManagerClick: () -> Unit
) {
    val context = LocalContext.current

    val products by viewModel.products.collectAsStateWithLifecycle()
    val selectedProduct by viewModel.selectedProduct.collectAsStateWithLifecycle()
    val enteredQuantity by viewModel.enteredQuantity.collectAsStateWithLifecycle()
    val total by viewModel.cashierTotal.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Header and Manager button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cash Register",
                    style = MaterialTheme.typography.headlineMedium
                )

                OutlinedButton(onClick = onManagerClick) {
                    Text("Manager")
                }
            }
        }

        // Selected product
        item {
            Text(
                text = "Selected Product: ${selectedProduct?.name ?: "None"}",
                style = MaterialTheme.typography.titleLarge
            )
        }

        // Keypad and Buy button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Number keypad
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val keypadRows = listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9")
                    )

                    keypadRows.forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            row.forEach { digit ->
                                OutlinedButton(
                                    onClick = {
                                        viewModel.enterDigit(digit)
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(
                                        text = digit,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }
                        }
                    }

                    // Clear and zero buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.clearQuantity()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                        ) {
                            Text("C")
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.enterDigit("0")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                        ) {
                            Text("0")
                        }

                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                // Total and Buy button
                Column(
                    modifier = Modifier.width(110.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Total")

                    Text(
                        text = "$${"%.2f".format(total)}",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Button(
                        onClick = {
                            val message = viewModel.buyProduct()

                            Toast.makeText(
                                context,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Buy")
                    }
                }

            }
        }

        // Quantity display
        item {
            Text(
                text = "Quantity: ${enteredQuantity.ifEmpty { "0" }}",
                style = MaterialTheme.typography.titleLarge
            )
        }

        // Product list
        item {
            Text(
                text = "Products",
                style = MaterialTheme.typography.titleLarge
            )
        }

        items(products) { product ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.selectProduct(product)
                    },
                colors = CardDefaults.cardColors(
                    containerColor =
                        if (selectedProduct?.id == product.id) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "$${"%.2f".format(product.price)}"
                        )
                    }

                    Text("Stock: ${product.quantity}")
                }
            }
        }
    }
}
