package com.shukri.wearablefit.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CaloriesDialogComponent(
    foodName: String,
    caloriesConsumed: Int,
    caloriesGoal: Int,
    carbsConsumed: Int,
    proteinConsumed: Int,
    fatConsumed: Int,
    onDismissRequest: () -> Unit,
    onSaveNutrition: (name: String, calories: Int, carbs: Int, protein: Int, fat: Int) -> Unit
) {
    var nameInput by remember { mutableStateOf(foodName) }
    var caloriesInput by remember { mutableStateOf(caloriesConsumed.toString()) }
    var carbsInput by remember { mutableStateOf(carbsConsumed.toString()) }
    var proteinInput by remember { mutableStateOf(proteinConsumed.toString()) }
    var fatInput by remember { mutableStateOf(fatConsumed.toString()) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🍎 Add Food",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))

                NutritionInput(label = "Food Name", value = nameInput, onValueChange = { nameInput = it }, isNumeric = false)
                NutritionProgressBar(consumed = caloriesConsumed, goal = caloriesGoal, label = "Calories")
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    NutritionInput(
                        label = "Calories",
                        value = caloriesInput,
                        onValueChange = { caloriesInput = it },
                        modifier = Modifier.weight(1f),
                        isNumeric = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    NutritionInput(
                        label = "Carbs",
                        value = carbsInput,
                        onValueChange = { carbsInput = it },
                        modifier = Modifier.weight(1f),
                        isNumeric = true
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    NutritionInput(
                        label = "Protein",
                        value = proteinInput,
                        onValueChange = { proteinInput = it },
                        modifier = Modifier.weight(1f),
                        isNumeric = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    NutritionInput(
                        label = "Fat",
                        value = fatInput,
                        onValueChange = { fatInput = it },
                        modifier = Modifier.weight(1f),
                        isNumeric = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSaveNutrition(
                                nameInput,
                                caloriesInput.toIntOrNull() ?: caloriesConsumed,
                                carbsInput.toIntOrNull() ?: carbsConsumed,
                                proteinInput.toIntOrNull() ?: proteinConsumed,
                                fatInput.toIntOrNull() ?: fatConsumed
                            )
                            onDismissRequest()
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun NutritionProgressBar(consumed: Int, goal: Int, label: String) {
    val progress = (consumed.toFloat() / goal).coerceIn(0f, 1f)
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(text = "$consumed / $goal", style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
        )
    }
}

@Composable
fun NutritionInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isNumeric: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.bodySmall) },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isNumeric) KeyboardType.Number else KeyboardType.Text
        ),
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        textStyle = MaterialTheme.typography.bodyMedium
    )
}

@Preview
@Composable
fun CaloriesDialogPreview() {
    MaterialTheme {
        CaloriesDialogComponent(
            foodName = "Apple",
            caloriesConsumed = 1500,
            caloriesGoal = 2000,
            carbsConsumed = 150,
            proteinConsumed = 75,
            fatConsumed = 50,
            onDismissRequest = {},
            onSaveNutrition = { _, _, _, _, _ -> }
        )
    }
}