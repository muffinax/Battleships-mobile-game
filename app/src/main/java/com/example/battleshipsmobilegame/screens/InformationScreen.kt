package com.example.battleshipsmobilegame.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SetupTurnScreen(
    player: String,
    onReadyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "$player turn"
        )
        Text(
            text = "Setup your battleships!"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onReadyClick) {
            Text(text = "Ready", fontSize = 24.sp)
        }
    }
}

@Composable
fun ChangeTurnScreen(
    player: String,
    result: String,
    onReadyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "$player turn"
        )

        if (result.isNotEmpty()) {
            Text(
                text = result
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onReadyClick) {
            Text(text = "Ready", fontSize = 24.sp)
        }
    }
}

@Composable
fun WinnerScreen(
    player: String,
    onPlayAgainClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "$player is the winner!"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onPlayAgainClick) {
            Text(text = "Play again!", fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onMenuClick) {
            Text(text = "Menu", fontSize = 24.sp)
        }
    }
}