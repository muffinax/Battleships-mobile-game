package com.example.battleshipsmobilegame.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.battleshipsmobilegame.CellState
import com.example.battleshipsmobilegame.components.BattleshipBoard

@Composable
fun ActionScreen(
    player: String,
    ownBoardState: (row: Int, col: Int) -> CellState,
    targetBoardState: (row: Int, col: Int) -> CellState,
    onTargetCellClick: (row: Int, col: Int) -> Unit,
    onTargetCellLongClick: (row: Int, col: Int) -> Unit,
    onEndTurnClick: () -> Unit,
    shotResultText: String,
    canShoot: Boolean,
    remainingEnemyShipsText: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "$player's board"
        )

        BattleshipBoard(
            modifier = Modifier.fillMaxWidth(0.6f), // 60% width
            boardState = ownBoardState,
            onCellClick = null,
            onCellLongClick = null
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Shoot your enemy Battleships"
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Shooting result
        Text(
            text = shotResultText,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = if (shotResultText == "Miss!") Color.Gray else Color.Red
        )

        Spacer(modifier = Modifier.height(6.dp))

        // attacking board
        BattleshipBoard(
            modifier = Modifier.fillMaxWidth(),
            boardState = targetBoardState,
            // null when can't shoot
            onCellClick = if (canShoot) onTargetCellClick else null,
            onCellLongClick = if (canShoot) onTargetCellLongClick else null
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "The remained ships:"
        )
        Text (remainingEnemyShipsText)

        Spacer(modifier = Modifier.height(8.dp))

        if (!canShoot) {
            Button(onClick = onEndTurnClick) {
                Text(text = "End Turn", fontSize = 24.sp)
            }
        }
    }
}