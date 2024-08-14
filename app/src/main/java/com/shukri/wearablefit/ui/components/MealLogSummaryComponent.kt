package com.shukri.wearablefit.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MealLogSummaryComponent(
    hasLoggedMeals: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "🍎",  // Apple emoji
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            if (!hasLoggedMeals) {
                Text(
                    text = "You have nothing logged for this day",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "Logged meals will appear here",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMealLogSummaryComponentEmpty() {
    MaterialTheme {
        MealLogSummaryComponent(hasLoggedMeals = false)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMealLogSummaryComponentWithMeals() {
    MaterialTheme {
        MealLogSummaryComponent(hasLoggedMeals = true)
    }
}
