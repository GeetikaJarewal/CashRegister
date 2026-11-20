package com.example.cashregister.viewmodel
import androidx.lifecycle.ViewModel
import com.example.cashregister.model.Product
import com.example.cashregister.repository.CashRegisterRepository
import com.example.cashregister.model.CartItem
import com.example.cashregister.model.Purchase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CashRegisterViewModel : ViewModel() { //ViewModel() is the parent class we are inheriting from
   private val repository =  CashRegisterRepository()

   //Available products
   private val _products = MutableStateFlow(repository.getProducts())
   val products: StateFlow<List<Product>> = _products.asStateFlow()

   //Shopping cart
   private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
   val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

   //Total amount
   private val _total = MutableStateFlow(0.0)
   val total: StateFlow<Double> = _total.asStateFlow()

   //Purchase history
   private val _purchaseHistory = MutableStateFlow(repository.getPurchases())
   val purchaseHistory: StateFlow<List<Purchase>> = _purchaseHistory.asStateFlow()

   //Add product to cart
   fun addToCart(product: Product) {
      val currentCart = _cartItems.value.toMutableList()
      val existingItem = currentCart.find {
         it.product.id == product.id
      }
      if (existingItem !=null){
         val updatedItem = existingItem.copy(
            quantity = existingItem.quantity +1
         )
         val index = currentCart.indexOf(existingItem)
         currentCart[index] = updatedItem
      } else {
         currentCart.add(
            CartItem(product, 1)
         )
      }
      _cartItems.value = currentCart
      calculateTotal()
   }

   //Increase quantity
   fun increaseQuantity(productId: Int) {
      _cartItems.value = _cartItems.value.map{ item ->
         if(item.product.id == productId) {
            item.copy(quantity = item.quantity + 1)
         } else {
            item
         }
      }
      calculateTotal()
   }
   //Decrease quantity
   fun decreaseQuantity(productId: Int){
      _cartItems.value = _cartItems.value.map {item ->
         if(item.product.id == productId){
            item.copy(quantity = item.quantity -1)
         } else {
            item
         }
      }.filter {it.quantity>0}
      calculateTotal()
   }

   //Remove product completely
   fun removeFromCart(productId: Int) {
      _cartItems.value = _cartItems.value.filter {
         it.product.id != productId
      }
      calculateTotal()
   }

   //Calculate total
   private fun calculateTotal() {
      _total.value = _cartItems.value.sumOf { item ->
         item.product.price * item.quantity
      }
   }

   //Complete purchase
   fun completePurchase(){
      if(_cartItems.value.isEmpty()) {
         return
      }
      val purchase = Purchase(
         id = repository.getPurchases().size + 1,
         items = _cartItems.value.toList(),
         total = _total.value,
         date = java.text.SimpleDateFormat (
            "yyyy-MM-dd HH:mm",
            java.util.Locale.getDefault()
         ) .format(java.util.Date())
      )
      repository.addPurchase(purchase)
      _purchaseHistory.value = repository.getPurchases()
      _cartItems.value = emptyList()
      calculateTotal()
   }
}