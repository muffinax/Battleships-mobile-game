package com.example.battleshipsmobilegame.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameParametersScreen(
    player1Name: String,
    onPlayer1NameChange: (String) -> Unit,
    player2Name: String,
    onPlayer2NameChange: (String) -> Unit,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ){
        Text(text = "Player 1:")

        OutlinedTextField(
            value = player1Name,
            onValueChange = onPlayer1NameChange,
            label = { Text("player 1") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Player 2:")

        OutlinedTextField(
            value = player2Name,
            onValueChange = onPlayer2NameChange,
            label = { Text("player 2") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            onPlayClick()
        }) {
            Text(text = "Play", fontSize = 24.sp)
        }
    }
}