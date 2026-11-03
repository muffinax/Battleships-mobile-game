package com.example.battleshipsmobilegame.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenuScreen(
    hasSavedGame: Boolean,
    onNewGameClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showWarningDialog by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Welcome to Battleships",
            fontSize = 30.sp,
        )
        Text(
            text = "Mobile game!",
            fontSize = 30.sp,
        )

        // Space
        Spacer(modifier = Modifier.height(48.dp))

        // New Game button
        Button(onClick = {
            if (hasSavedGame) {
                // If there is a saved game, show warning dialog
                showWarningDialog = true
            } else {
                // If no saved game, proceed directly to game settings
                onNewGameClick()
            }
        }) {
            Text(text = "New Game", fontSize = 24.sp)
        }

        // Resume Game button - visible only if there is a saved game
        if (hasSavedGame) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /* resume mechanics here */ }) {
                Text(text = "Resume Game", fontSize = 24.sp)
            }
        }
    }

    if (showWarningDialog) {
        AlertDialog(
            onDismissRequest = { showWarningDialog = false },
            title = { Text("Warning") },
            text = { Text("Starting a new game will erase your saved game. Continue?") },
            confirmButton = {
                Button(onClick = {
                    showWarningDialog = false
                    // erase saved game and proceed
                    onNewGameClick()
                }) { Text("Yes") }
            },
            dismissButton = {
                Button(onClick = { showWarningDialog = false }) { Text("Cancel") }
            }
        )
    }
}