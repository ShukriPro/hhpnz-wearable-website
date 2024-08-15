package com.shukri.wearablefit.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BottomNavigationComponent() {
    var selectedItem by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    NavigationBar(
        containerColor = Color.White, // Background color (Purple as an example)

    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            selected = selectedItem == 0,
            onClick = { selectedItem = 0 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AddCircle, contentDescription = null) },
            selected = selectedItem == 1,
            onClick = { showDialog = true }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = null) },
            selected = selectedItem == 2,
            onClick = { selectedItem = 2 }
        )
    }

    // Conditionally show the CaloriesDialogComponent when showDialog is true

    // Conditionally show the CaloriesDialogComponent when showDialog is true
    if (showDialog) {
        CaloriesDialogComponent(
            caloriesConsumed = 1500,
            caloriesGoal = 2000,
            carbsConsumed = 150,
            proteinConsumed = 75,
            fatConsumed = 50,
            onDismissRequest = { showDialog = false },
            foodName = "Apple",
            onSaveNutrition = { food ,calories, carbs, protein, fat ->
                // Handle the new nutrition data here
                println("New Calories: $calories, Carbs: $carbs, Protein: $protein, Fat: $fat")
                showDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationComponentPreview() {
    BottomNavigationComponent()
}