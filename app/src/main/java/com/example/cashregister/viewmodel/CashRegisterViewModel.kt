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

   // Currently selected product
   private val _selectedProduct = MutableStateFlow<Product?>(null)
   val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

   // Quantity entered using the number keypad
   private val _enteredQuantity = MutableStateFlow("")
   val enteredQuantity: StateFlow<String> = _enteredQuantity.asStateFlow()

   // Total price for the selected product
   private val _cashierTotal = MutableStateFlow(0.0)
   val cashierTotal: StateFlow<Double> = _cashierTotal.asStateFlow()

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

   // Select a product
   fun selectProduct(product: Product) {
      _selectedProduct.value = product
      _enteredQuantity.value = ""
      _cashierTotal.value = 0.0
   }

   // Enter a number from the keypad
   fun enterDigit(digit: String) {
      if (digit.length != 1 || digit !in "0123456789") return

      val newQuantity = _enteredQuantity.value + digit

      _enteredQuantity.value = newQuantity
      calculateCashierTotal()
   }

   // Clear entered quantity
   fun clearQuantity() {
      _enteredQuantity.value = ""
      _cashierTotal.value = 0.0
   }

   // Calculate cashier total
   private fun calculateCashierTotal() {
      val product = _selectedProduct.value
      val quantity = _enteredQuantity.value.toIntOrNull() ?: 0

      _cashierTotal.value = if (product != null) {
         product.price * quantity
      } else {
         0.0
      }
   }

   fun buyProduct(): String {

      val product = _selectedProduct.value

      if (product == null) {
         return "Please select a product"
      }

      val quantity = _enteredQuantity.value.toIntOrNull()

      if (quantity == null || quantity <= 0) {
         return "Please enter a valid quantity"
      }

      if (quantity > product.quantity) {
         return "Not enough stock available"
      }

      // Calculate purchase total
      val purchaseTotal = product.price * quantity

      // Reduce available stock
      val success = repository.reduceStock(product.id, quantity)

      if (!success) {
         return "Unable to complete purchase"
      }

      // Update the product list displayed by the app
      _products.value = repository.getProducts()

      // Create purchase record
      val purchase = Purchase(
         id = repository.getPurchases().size + 1,
         items = listOf(CartItem(product, quantity)),
         total = purchaseTotal,
         date = java.text.SimpleDateFormat(
            "yyyy-MM-dd HH:mm",
            java.util.Locale.getDefault()
         ).format(java.util.Date())
      )

      // Save purchase in history
      repository.addPurchase(purchase)
      _purchaseHistory.value = repository.getPurchases()

      // Reset cashier screen
      _selectedProduct.value = null
      _enteredQuantity.value = ""
      _cashierTotal.value = 0.0

      return "Purchase successful!"
   }
   // Restock an existing product
   fun restockProduct(productId: Int, amount: Int): Boolean {

      if (amount <= 0) {
         return false
      }

      val success = repository.restockProduct(productId, amount)

      if (success) {
         // Refresh the product list with updated stock
         _products.value = repository.getProducts()
      }

      return success
   }

}
