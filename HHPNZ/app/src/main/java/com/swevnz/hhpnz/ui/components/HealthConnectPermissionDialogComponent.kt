package com.swevnz.hhpnz.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swevnz.hhpnz.R
import com.swevnz.hhpnz.ui.screens.HealthConnectSyncScreen

@Composable
fun HealthConnectPermissionDialog() {
    // State to track if the user clicked the button to navigate to the next screen
    var navigateToHealthConnectSync by remember { mutableStateOf(false) }

    if (navigateToHealthConnectSync) {
        // Show the HealthConnectSyncScreen when the button is clicked
        HealthConnectSyncScreen()
    } else {
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp)
                .clip(RoundedCornerShape(28.dp)),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Sync HHPNZ with Health Connect",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Please grant HHPNZ permission to access and sync your health data through Health Connect.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Set the state to true to trigger navigation to the HealthConnectSyncScreen
                        navigateToHealthConnectSync = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                ) {
                    Text("Get started", color = Color.White)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HealthConnectPermissionDialogPreview() {
    MaterialTheme {
        HealthConnectPermissionDialog()
    }
}