
package com.example.cashregister.repository

import com.example.cashregister.model.Product
import com.example.cashregister.model.Purchase

class CashRegisterRepository {

    // Stores all available products and their stock quantities
    private val products = mutableListOf(  //mutableListOf creates a list that we cannot modify
        Product(1, "Apple", 1.50, 20),
        Product(2, "Bread", 3.00, 15),
        Product(3, "Milk", 4.00, 10),
        Product(4, "Bananas", 2.50, 25),
        Product(5, "Eggs", 5.00, 12),
        Product(6, "Orange Juice", 3.50, 18)
    )

    // Stores completed purchases
    private val purchases = mutableListOf<Purchase>()

    // Returns the current product list
    fun getProducts(): List<Product> {
        return products.toList()
    }

    // Reduces stock after a successful purchase
    fun reduceStock(productId: Int, amount: Int): Boolean {
        val product = products.find { it.id == productId }

        if (product == null || amount <= 0 || amount > product.quantity) {
            return false
        }

        val index = products.indexOf(product)

        products[index] = product.copy(
            quantity = product.quantity - amount
        )

        return true
    }

    // Adds stock when the manager restocks a product
    fun restockProduct(productId: Int, amount: Int): Boolean {
        val product = products.find { it.id == productId }

        if (product == null || amount <= 0) {
            return false
        }

        val index = products.indexOf(product)

        products[index] = product.copy(
            quantity = product.quantity + amount
        )

        return true
    }

    // Saves a completed purchase
    fun addPurchase(purchase: Purchase) {
        purchases.add(purchase)
    }

    // Returns purchase history
    fun getPurchases(): List<Purchase> {
        return purchases.toList()
    }
}
