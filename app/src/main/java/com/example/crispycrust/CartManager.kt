package com.example.crispycrust

import androidx.compose.runtime.mutableStateListOf

object CartManager {
    val cartItems = mutableStateListOf<CartItem>()

    fun addItems(newItems: List<MenuItem>) {
        newItems.forEach { menuItem ->
            val priceValue = menuItem.price.replace("£", "").toDoubleOrNull() ?: return@forEach
            val existing = cartItems.find { it.name == menuItem.name }
            if (existing != null) {
                val updated = existing.copy(quantity = existing.quantity + 1)
                val index = cartItems.indexOf(existing)
                if (index >= 0) {
                    cartItems[index] = updated
                }
            } else {
                cartItems.add(
                    CartItem(
                        name = menuItem.name,
                        price = priceValue,
                        quantity = 1,
                        imageRes = menuItem.imageRes
                    )
                )
            }
        }
    }

    fun increase(item: CartItem) {
        val index = cartItems.indexOfFirst { it.name == item.name }
        if (index >= 0) {
            cartItems[index] = cartItems[index].copy(quantity = cartItems[index].quantity + 1)
        }
    }

    fun decrease(item: CartItem) {
        val index = cartItems.indexOfFirst { it.name == item.name }
        if (index >= 0) {
            val currentItem = cartItems[index]
            if (currentItem.quantity > 1) {
                cartItems[index] = currentItem.copy(quantity = currentItem.quantity - 1)
            } else {
                cartItems.removeAt(index)
            }
        }
    }

    fun remove(item: CartItem) {
        val index = cartItems.indexOfFirst { it.name == item.name }
        if (index >= 0) {
            cartItems.removeAt(index)
        }
    }

    fun clear() {
        cartItems.clear()
    }

    fun getTotal(): Double {
        return cartItems.sumOf { it.price * it.quantity }
    }
}
