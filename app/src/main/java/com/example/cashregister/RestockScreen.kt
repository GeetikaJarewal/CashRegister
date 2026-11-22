
package com.example.cashregister

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cashregister.viewmodel.CashRegisterViewModel

@Composable
fun RestockScreen(
    viewModel: CashRegisterViewModel,
    onCancelClick: () -> Unit
) {
    val context = LocalContext.current

    val products by viewModel.products.collectAsStateWithLifecycle()

    var selectedProductId by remember { mutableStateOf<Int?>(null) }
    var restockQuantity by remember { mutableStateOf("") }

    val selectedProduct = products.find {
        it.id == selectedProductId
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Restock Products",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select a Product",
            style = MaterialTheme.typography.titleLarge
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(products) { product ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            selectedProductId = product.id
                        },
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (selectedProductId == product.id) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(product.name)
                        Text("Stock: ${product.quantity}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Selected: ${selectedProduct?.name ?: "None"}"
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = restockQuantity,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    restockQuantity = newValue
                }
            },
            label = {
                Text("Quantity to add")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                when {
                    selectedProduct == null -> {
                        Toast.makeText(
                            context,
                            "Please select a product",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    restockQuantity.isBlank() ||
                            (restockQuantity.toIntOrNull() ?: 0) <= 0 -> {
                        Toast.makeText(
                            context,
                            "Please enter a valid quantity",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        val success = viewModel.restockProduct(
                            selectedProduct.id,
                            restockQuantity.toInt()
                        )

                        if (success) {
                            Toast.makeText(
                                context,
                                "Stock updated successfully!",
                                Toast.LENGTH_SHORT
                            ).show()

                            onCancelClick()
                        } else {
                            Toast.makeText(
                                context,
                                "Unable to update stock",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("OK - Restock")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onCancelClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}
