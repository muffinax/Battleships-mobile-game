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
import com.example.battleshipsmobilegame.CellState
import com.example.battleshipsmobilegame.components.BattleshipBoard

@Composable
fun SetupBattleShipsScreen(
    boardState: (row: Int, col: Int) -> CellState,
    onCellClick: ((row: Int, col: Int) -> Unit)?,
    onReadyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Setup your battleships!"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "The remained ships:"
        )

        //Text (amount of ships that are left to be placed)

        Spacer(modifier = Modifier.height(16.dp))

        BattleshipBoard(
            boardState = boardState,
            onCellClick = onCellClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onReadyClick) {
            Text(text = "Confirm", fontSize = 24.sp)
        }
    }
}