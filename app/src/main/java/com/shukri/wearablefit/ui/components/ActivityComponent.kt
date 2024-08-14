package com.shukri.wearablefit.ui.components

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActivityComponent(
    steps: Long,
    sleepDuration: Long,
    weight: Double,
    heartRate: Int
) {
    val metrics = listOf(
        MetricData("Steps", steps, "steps", 10000),
        MetricData("Sleep", sleepDuration, "min", 480),
        MetricData("Weight", weight, "kg"),
        MetricData("Heart Rate", heartRate.toLong(), "bpm")
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        items(metrics) { metric ->
            MetricCard(
                title = metric.title,
                value = metric.value,
                unit = metric.unit,
                goal = metric.goal
            )
        }
    }
}

@Composable
fun MetricCard(title: String, value: Number, unit: String, goal: Number? = null) {
    Card(
        modifier = Modifier
            .width(85.dp)
            .height(100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = value.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = unit,
                fontSize = 10.sp,
                color = Color.Gray
            )
            if (goal != null) {
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = (value.toFloat() / goal.toFloat()).coerceIn(0f, 1f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = Color(0xFFFFA500)
                )
                Text(
                    text = "${value} / ${goal}",
                    fontSize = 10.sp,
                    color = Color.Gray,
                )
            }
        }
    }
}

data class MetricData(
    val title: String,
    val value: Number,
    val unit: String,
    val goal: Number? = null
)

@Preview(showBackground = true)
@Composable
fun PreviewActivityComponent() {
    ActivityComponent(
        steps = 7500,
        sleepDuration = 420,
        weight = 70.5,
        heartRate = 72
    )
}