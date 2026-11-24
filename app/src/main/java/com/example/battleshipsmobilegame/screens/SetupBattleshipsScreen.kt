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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.battleshipsmobilegame.CellState
import com.example.battleshipsmobilegame.components.BattleshipBoard

@Composable
fun SetupBattleShipsScreen(
    player: String,
    boardState: (row: Int, col: Int) -> CellState,
    onCellClick: ((row: Int, col: Int) -> Unit)?,
    onReadyClick: () -> Unit,
    isConfirmEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "$player: Setup your battleships!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "The remained ships:")

        Text(text = "Required fleet:")
        Text(text = "1 x 4-cells, 2 x 3-cells, 3 x 2-cells, 4 x 1-cell")

        Spacer(modifier = Modifier.height(16.dp))

        BattleshipBoard(
            boardState = boardState,
            onCellClick = onCellClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onReadyClick,
            enabled = isConfirmEnabled // Blocking button
        ) {
            Text(text = "Confirm", fontSize = 24.sp)
        }
    }
}