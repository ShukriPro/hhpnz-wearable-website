package com.shukri.wearablefit.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun InfoComponent(modifier: Modifier = Modifier) {
    val currentDate = LocalDate.now()
    val dayFormatter = DateTimeFormatter.ofPattern("EEEE")
    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM")

    Column(modifier = modifier) {
        Text(
            text = "Today",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${currentDate.format(dayFormatter)} ${currentDate.format(dateFormatter)}",
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInfoComponent() {
    MaterialTheme {
        InfoComponent()
    }
}