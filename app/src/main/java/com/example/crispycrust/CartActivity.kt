package com.example.crispycrust

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
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

data class CartItem(
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageRes: Int
)

val sampleCartItems = listOf(
    CartItem("BBQ Chicken Pizza", 9.49, 1, R.drawable.bbq_chicken_pizza),
    CartItem("Pepsi", 1.50, 2, R.drawable.pepsi),
    CartItem("Garlic Dip", 0.99, 3, R.drawable.garlic_dip)
)

class CartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CrispyCrustTheme {
                CartScreen()
            }
        }
    }
}

@Composable
fun CartScreen() {
    val context = LocalContext.current
    val cartItems = remember { sampleCartItems }
    val total = cartItems.sumOf { it.price * it.quantity }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total:", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Text("£${"%.2f".format(total)}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = {
                            context.startActivity(Intent(context, PaymentActivity::class.java))
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Payment, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Proceed to Pay")
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Orange40)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    "Your Cart",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(cartItems) { item ->
                        CartItemCard(item)
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(item: CartItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Qty: ${item.quantity}", fontSize = 14.sp)
            }
            Text("£${"%.2f".format(item.price * item.quantity)}", fontWeight = FontWeight.Bold)
        }
    }
}
