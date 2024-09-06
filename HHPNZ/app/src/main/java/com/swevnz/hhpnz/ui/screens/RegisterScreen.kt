package com.swevnz.hhpnz.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.swevnz.hhpnz.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    val activity = context as? Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,

            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.hhpnz),
                contentDescription = "HHPNZ Logo",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "HHPNZ",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it.replace(" ", "") }, // Remove spaces as the user types
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Text(
                    text = if (passwordVisible) "Hide" else "Show",
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it.replace(" ", "") }, // Remove spaces as the user types
            label = { Text("Confirm Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), // Same visibility toggle as password
            trailingIcon = {
                Text(
                    text = if (passwordVisible) "Hide" else "Show",
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }, // Toggles both fields
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            ),
            singleLine = true
        )

        Button(
            onClick = {
                if (validateInputs(context, email, password, confirmPassword)) {
                    isLoading = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                user?.sendEmailVerification()
                                    ?.addOnCompleteListener { verifyTask ->
                                        if (verifyTask.isSuccessful) {
                                            showToast(context, "Registration successful. Verification email sent.")
                                            navController.popBackStack()
                                        } else {
                                            showToast(context, "Failed to send verification email.")
                                        }
                                    }
                            } else {
                                showToast(context, "Registration failed: ${task.exception?.message}")
                            }
                        }
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Register", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add "Back to Login" text link
        TextButton(
            onClick = {
                navController.popBackStack() // This will navigate back to the previous screen
            }
        ) {
            Text(text = "Back to Login")
        }
    }
}

fun validateInputs(context: android.content.Context, email: String, password: String, confirmPassword: String): Boolean {
    return when {
        email.isBlank() -> {
            showToast(context, "Email cannot be empty")
            false
        }
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
            showToast(context, "Invalid email format")
            false
        }
        password.isBlank() -> {
            showToast(context, "Password cannot be empty")
            false
        }
        password.length < 6 -> {
            showToast(context, "Password must be at least 6 characters long")
            false
        }
        password != confirmPassword -> {
            showToast(context, "Passwords do not match")
            false
        }
        else -> true
    }
}

fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    //RegisterScreen(navController: NavHostController)
}