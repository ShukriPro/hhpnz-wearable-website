package com.swevnz.hhpnz.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.swevnz.hhpnz.R
import com.swevnz.hhpnz.ui.theme.HHPNZTheme

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSignedIn by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()

    val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    handleGoogleSignInResult(account, auth, context) {
                        isSignedIn = true
                    }
                } else {
                    // Handle sign-in cancellation or failure
                    android.widget.Toast.makeText(context, "Google Sign-In canceled or failed", android.widget.Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                // Handle the ApiException
                android.widget.Toast.makeText(context, "Google Sign-In failed: ${e.statusCode}", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    )

    if (isSignedIn) {
        HHPNZTheme {
            HomeScreen()
        }
    } else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
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
                    text = "Sign in to continue",
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
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    )
                )

                Button(
                    onClick = {
                        signInUser(email, password, auth, context) {
                            isSignedIn = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text("Log In", style = MaterialTheme.typography.titleMedium)
                }

                TextButton(
                    onClick = {
                        onForgotPasswordClick(email, context)
                    }
                ) {
                    Text(
                        text = "Forgot Password?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "OR",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Divider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedButton(
                    onClick = {
                        val signInIntent = googleSignInClient.signInIntent
                        googleSignInLauncher.launch(signInIntent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF4285F4), // Google Blue
                        containerColor = Color.White
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
                ) {
                    Text("Sign in with Google", style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Don't have an account? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    TextButton(onClick = onNavigateToRegister) {
                        Text(
                            text = "Register",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

private fun onForgotPasswordClick(email: String, context: Context) {
    if (email.isNotBlank()) {
        // Use FirebaseAuth to send the password reset email
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Show a toast when the reset email is successfully sent
                    Toast.makeText(context, "Password reset email sent to $email", Toast.LENGTH_SHORT).show()
                } else {
                    // Show a toast for failure (e.g., if the email is not registered)
                    Toast.makeText(context, "Failed to send reset email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    } else {
        // Show a toast if the email field is empty
        Toast.makeText(context, "Please enter your email address", Toast.LENGTH_SHORT).show()
    }
}


private fun signInUser(email: String, password: String, auth: FirebaseAuth, context: android.content.Context, onSignInSuccess: () -> Unit) {
    if (email.isBlank() || password.isBlank()) {
        Toast.makeText(context, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show()
        return
    }

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignInSuccess()
            } else {
                Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}


private fun handleGoogleSignInResult(
    account: GoogleSignInAccount?,
    auth: FirebaseAuth,
    context: android.content.Context,
    onSignInSuccess: () -> Unit
) {
    try {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign-in success, call the onSignInSuccess callback to navigate to HomeScreen
                    android.widget.Toast.makeText(context, "Google Sign-In Successful", android.widget.Toast.LENGTH_SHORT).show()
                    onSignInSuccess()
                } else {
                    // Sign-in failed, display a message to the user
                    android.widget.Toast.makeText(context, "Google Sign-In Failed", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
    } catch (e: Exception) {
        // Handle any errors during the sign-in process
        android.widget.Toast.makeText(context, "Google Sign-In Error: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
    }
}



@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    HHPNZTheme {
        AppNavigation()
    }
}