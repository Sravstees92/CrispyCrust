package com.example.crispycrust

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.crispycrust.ui.theme.CrispyCrustTheme

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
    val filteredItems = remember(selectedCategory) {
        if (selectedCategory == "All") sampleItems
        else sampleItems.filter { it.category == selectedCategory }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar() }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)) {

            Text("Crispy Crust Menu", fontSize = 24.sp, fontWeight = FontWeight.Bold)
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
                    MenuCard(item)
                }
            }
        }
    }
}

@Composable
fun MenuCard(item: MenuItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
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
fun BottomNavigationBar() {
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
            onClick = { /* TODO: navigate to Orders screen */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") },
            selected = false,
            onClick = { /* TODO: navigate to Cart */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Payment, contentDescription = "Payment") },
            label = { Text("Payment") },
            selected = false,
            onClick = { /* TODO: navigate to Payment screen */ }
        )
    }
}
