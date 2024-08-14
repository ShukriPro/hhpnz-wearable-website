
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NutritionTrackerComponent(
    carbsCurrent: Int,
    carbsTotal: Int,
    proteinCurrent: Int,
    proteinTotal: Int,
    fatCurrent: Int,
    fatTotal: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NutrientBar("Carbs", carbsCurrent, carbsTotal, Color(0xFF64B5F6))
            NutrientBar("Protein", proteinCurrent, proteinTotal, Color(0xFF81C784))
            NutrientBar("Fat", fatCurrent, fatTotal, Color(0xFFFFB74D))
        }
    }
}

@Composable
fun NutrientBar(
    nutrientName: String,
    current: Int,
    total: Int,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        Text(
            text = nutrientName,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Box(
            modifier = Modifier
                .height(6.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(3.dp))
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(current.toFloat() / total)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
        }
        Text(
            text = "$current/$total",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNutritionTrackerComponent() {
    MaterialTheme {
        NutritionTrackerComponent(
            carbsCurrent = 44,
            carbsTotal = 316,
            proteinCurrent = 10,
            proteinTotal = 126,
            fatCurrent = 4,
            fatTotal = 84
        )
    }
}