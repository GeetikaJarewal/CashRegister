
package com.example.cashregister

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cashregister.model.CartItem
import com.example.cashregister.model.Product
import com.example.cashregister.viewmodel.CashRegisterViewModel

@Composable
fun MainScreen(
    viewModel: CashRegisterViewModel,
    onHistoryClick: () -> Unit
) {
    // Observe the ViewModel states
    val products by viewModel.products.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val total by viewModel.total.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // App heading
        Text(
            text = "Cash Register",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Available products heading
        Text(
            text = "Available Products",
            style = MaterialTheme.typography.titleLarge
        )

        // Display product list
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    onAddClick = {
                        viewModel.addToCart(product)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Shopping cart heading
        Text(
            text = "Shopping Cart",
            style = MaterialTheme.typography.titleLarge
        )

        // Display cart items
        CartSection(
            cartItems = cartItems,
            onIncrease = { productId ->
                viewModel.increaseQuantity(productId)
            },
            onDecrease = { productId ->
                viewModel.decreaseQuantity(productId)
            },
            onRemove = { productId ->
                viewModel.removeFromCart(productId)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Display total
        Text(
            text = "Total: $${"%.2f".format(total)}",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Complete purchase button
        Button(
            onClick = {
                viewModel.completePurchase()
            },
            enabled = cartItems.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Complete Purchase")
        }

        // Navigate to purchase history
        OutlinedButton(
            onClick = onHistoryClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Purchase History")
        }
    }
}


// Individual product card
@Composable
fun ProductCard(
    product: Product,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
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

            Button(onClick = onAddClick) {
                Text("Add")
            }
        }
    }
}


// Shopping cart section
@Composable
fun CartSection(
    cartItems: List<CartItem>,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onRemove: (Int) -> Unit
) {
    Column {

        if (cartItems.isEmpty()) {

            Text("Your cart is empty")

        } else {

            cartItems.forEach { item ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column {
                        Text(item.product.name)

                        Text("Quantity: ${item.quantity}")

                        Text(
                            "Subtotal: $${"%.2f".format(item.product.price * item.quantity)}"
                        )
                    }

                    Row {

                        TextButton(
                            onClick = {
                                onDecrease(item.product.id)
                            }
                        ) {
                            Text("-")
                        }

                        TextButton(
                            onClick = {
                                onIncrease(item.product.id)
                            }
                        ) {
                            Text("+")
                        }

                        TextButton(
                            onClick = {
                                onRemove(item.product.id)
                            }
                        ) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
    }
}
