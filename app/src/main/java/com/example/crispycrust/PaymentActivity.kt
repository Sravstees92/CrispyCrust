package com.example.crispycrust

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.crispycrust.ui.theme.CrispyCrustTheme
import com.example.crispycrust.ui.theme.Orange40

class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CrispyCrustTheme {
                PaymentScreen()
            }
        }
    }
}

@Composable
fun PaymentScreen() {
    val context = LocalContext.current
    val deliveryFee = 2.0
    val cartTotal = sampleCartItems.sumOf { it.price * it.quantity }
    val totalPayable = cartTotal + deliveryFee

    var selectedMethod by remember { mutableStateOf("Card") }

    val paymentOptions = listOf("Card", "Cash on Delivery", "UPI")

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                Button(
                    onClick = {
                        Toast.makeText(context, "Payment Successful via $selectedMethod", Toast.LENGTH_LONG).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Done, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pay Now - £${"%.2f".format(totalPayable)}")
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
                    .padding(24.dp)
            ) {
                Text("Payment", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)

                Spacer(modifier = Modifier.height(24.dp))

                Text("Order Summary", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))

                Text("Items Total: £${"%.2f".format(cartTotal)}", color = Color.White)
                Text("Delivery Fee: £${"%.2f".format(deliveryFee)}", color = Color.White)
                Text("Total: £${"%.2f".format(totalPayable)}", fontWeight = FontWeight.Bold, color = Color.White)

                Spacer(modifier = Modifier.height(32.dp))

                Text("Choose Payment Method", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))

                paymentOptions.forEach { method ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (method == selectedMethod),
                                onClick = { selectedMethod = method }
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (method == selectedMethod),
                            onClick = { selectedMethod = method }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = method, fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
