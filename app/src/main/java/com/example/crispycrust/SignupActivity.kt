package com.example.crispycrust

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crispycrust.ui.theme.CrispyCrustTheme
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContent {
            CrispyCrustTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SignupScreen(auth)
                }
            }
        }
    }
}

@Composable
fun SignupScreen(auth: FirebaseAuth) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(3.dp),
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(100.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Create your account", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        trailingIcon = {
                            val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(icon, contentDescription = "Toggle Password Visibility")
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        trailingIcon = {
                            val icon = if (confirmVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { confirmVisible = !confirmVisible }) {
                                Icon(icon, contentDescription = "Toggle Confirm Visibility")
                            }
                        },
                        visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (errorMessage.isNotEmpty()) {
                        Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Button(
                        onClick = {
                            when {
                                fullName.isBlank() -> errorMessage = "Full name is required"
                                email.isBlank() -> errorMessage = "Email is required"
                                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> errorMessage = "Invalid email"
                                password.length < 6 -> errorMessage = "Password must be at least 6 characters"
                                password != confirmPassword -> errorMessage = "Passwords do not match"
                                else -> {
                                    errorMessage = ""
                                    isLoading = true

                                    auth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener { task ->
                                            isLoading = false
                                            if (task.isSuccessful) {
                                                Toast.makeText(context, "Account created!", Toast.LENGTH_SHORT).show()
                                                context.startActivity(Intent(context, LoginActivity::class.java))
                                                (context as? SignupActivity)?.finish()
                                            } else {
                                                errorMessage = task.exception?.localizedMessage ?: "Signup failed"
                                            }
                                        }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading
                    ) {
                        Text(if (isLoading) "Creating..." else "Sign Up")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = {
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                        (context as? SignupActivity)?.finish()
                    }) {
                        Text("Already have an account? Log in")
                    }
                }
            }
        }
    }
}
