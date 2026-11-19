package com.example.assignment1_cashregister.model

data class Product( //data class designed to hold data, Product is the name of our class
    val id: Int,    //id is Unique product identification
    val name: String, //name is Product name
    val price: Double, // val creates a read only property
)