package com.swevnz.hhpnz.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.swevnz.hhpnz.ui.components.ActivityScreen
import com.swevnz.hhpnz.ui.components.BottomNavigationComponent
import com.swevnz.hhpnz.ui.components.DateComponent
import com.swevnz.hhpnz.ui.theme.HHPNZTheme

@Composable
fun HomeScreen() {
    Scaffold(
        bottomBar = {
            HHPNZTheme {
                BottomNavigationComponent()
            }
        }
    ) { paddingValues ->
        // Create the Column to hold the ActivityScreen
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // This uses the theme's background color
                .padding(paddingValues)
                .padding(10.dp), // Add padding
            verticalArrangement = Arrangement.spacedBy(15.dp) // Spacing between elements
        ) {
            // Call ActivityScreen to display the activity data
            // Add the DateComponent as a separate item
            item {
                DateComponent()
            }

            item {
                ActivityScreen()
            }

        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HHPNZTheme {
        HomeScreen()
    }
}