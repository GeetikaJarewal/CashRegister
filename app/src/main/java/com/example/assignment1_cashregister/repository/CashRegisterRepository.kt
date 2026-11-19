package com.example.assignment1_cashregister.repository

import com.example.cashregister.model.Product
import com.example.cashregister.model.Purchase

class CashRegisterRepository{
    private val products = listOf(  //Creates a read-only list of products.
        Product(1, "Apple", 1.50),
        Product(2, "Bread", 3.00),
        Product(3, "Milk",4.00),
        Product(4, "Bananas", 5.00),
        Product(5, "Orange Juice", 3.50),
    )
    private val purchases = mutableListOf<Purchase>() //Creates a list that we can change.
    //purchases list must be mutable because customers will meke new purchases.

    fun getProducts(): List<Product>{  //getProducts() returns products
        return products
    }

    fun getPurchases(): List<Purchase>{ //getPurchases() returns completed purchases.
        return purchases.toList()
    }

    fun addPurchase(purchase: Purchase){  //addPurchase() adds a new transaction.
        purchase.add(purchase)
    }
}
//Note: the repository doesn't know anything about buttons or compose. This separation is important in MVVM.