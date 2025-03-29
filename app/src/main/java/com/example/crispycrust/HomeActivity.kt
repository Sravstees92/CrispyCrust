package com.example.crispycrust

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.crispycrust.ui.theme.CrispyCrustTheme
import com.example.crispycrust.ui.theme.Orange40
import androidx.compose.ui.draw.shadow



class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CrispyCrustTheme {
                HomeScreen()
            }
        }
    }
}

data class MenuItem(
    val name: String,
    val price: String,
    val imageRes: Int,
    val category: String
)

val categories = listOf("All", "Chicken Pizzas", "Milkshakes", "Desserts", "Soft Drinks", "Dips")

val sampleItems = listOf(
    MenuItem("Pepperoni Pizza", "£8.99", R.drawable.pepparoni_pizza, "Chicken Pizzas"),
    MenuItem("BBQ Chicken Pizza", "£9.49", R.drawable.bbq_chicken_pizza, "Chicken Pizzas"),
    MenuItem("Strawberry Shake", "£3.50", R.drawable.strawberry_shake, "Milkshakes"),
    MenuItem("Chocolate Shake", "£3.80", R.drawable.chocolate_shake, "Milkshakes"),
    MenuItem("Choco Lava Cake", "£4.00", R.drawable.choco_lava_cake, "Desserts"),
    MenuItem("Pepsi", "£1.50", R.drawable.pepsi, "Soft Drinks"),
    MenuItem("Garlic Dip", "£0.99", R.drawable.garlic_dip, "Dips")
)

@Composable
fun HomeScreen() {
    var selectedCategory by remember { mutableStateOf("All") }
    val context = LocalContext.current
    val selectedItems = remember { mutableStateListOf<MenuItem>() }

    val filteredItems = remember(selectedCategory) {
        if (selectedCategory == "All") sampleItems
        else sampleItems.filter { it.category == selectedCategory }
    }

    Scaffold(
        bottomBar = {
            Column {
                if (selectedItems.isNotEmpty()) {
                    BottomPopupCart(
                        selectedCount = selectedItems.size,
                        onAddToCart = {
                            CartManager.addItems(selectedItems)
                            selectedItems.clear()
                            context.startActivity(Intent(context, CartActivity::class.java))
                        }

                    )
                }

                BottomNavigationBar(
                    onOrdersClick = {
                        context.startActivity(Intent(context, OrdersActivity::class.java))
                    },
                    onCartClick = {
                        context.startActivity(Intent(context, CartActivity::class.java))
                    },
                    onPaymentClick = {
                        context.startActivity(Intent(context, PaymentActivity::class.java))
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Orange40)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    "Crispy Crust Menu",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredItems) { item ->
                        val isSelected = item in selectedItems
                        MenuCard(item, isSelected = isSelected) {
                            if (isSelected) selectedItems.remove(item)
                            else selectedItems.add(item)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MenuCard(item: MenuItem, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val backgroundColor = if (isSelected) Color(0xFFFFE0B2) else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(item.price, color = Color.Gray)
            }
        }
    }
}

@Composable
fun BottomPopupCart(
    selectedCount: Int,
    onAddToCart: () -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "$selectedCount item(s) selected",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Button(
                onClick = onAddToCart,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Add to Cart")
            }
        }
    }
}


@Composable
fun BottomNavigationBar(
    onOrdersClick: () -> Unit,
    onCartClick: () -> Unit,
    onPaymentClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = true,
            onClick = { /* Already on Home */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ListAlt, contentDescription = "Orders") },
            label = { Text("Orders") },
            selected = false,
            onClick = onOrdersClick
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") },
            selected = false,
            onClick = onCartClick
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Payment, contentDescription = "Payment") },
            label = { Text("Payment") },
            selected = false,
            onClick = onPaymentClick
        )
    }
}
