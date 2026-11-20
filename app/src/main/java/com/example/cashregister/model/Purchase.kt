package com.example.cashregister.model

data class Purchase(  //Purchaser can contain multiple products.
    val id: Int,
    val items: List<CartItem>,  // stores all the purchased items
    val total: Double,    //total stores the final amount
    val date: String      //date records when the purchase happened
)

data class CartItem(   //represents  one product inside a transaction, including its quantity
    val product: Product,
    val quantity: Int
)
//We use separate CartItem class because a product and the quantity purchased are two different things.