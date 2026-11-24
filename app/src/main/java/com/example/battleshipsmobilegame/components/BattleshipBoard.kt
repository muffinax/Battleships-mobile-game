package com.example.battleshipsmobilegame.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.battleshipsmobilegame.CellState

@Composable
fun BattleshipBoard(
    modifier: Modifier = Modifier,
    boardState: (row: Int, col: Int) -> CellState,
    onCellClick: ((row: Int, col: Int) -> Unit)? = null,
    onCellLongClick: ((row: Int, col: Int) -> Unit)? = null
) {
    val boardSize = 10

    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f) // Forces a 1:1 aspect ratio
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.weight(1f)) {
            // Empty top-left corner
            Box(modifier = Modifier.weight(1f))
            for (col in 1 until boardSize + 1) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = "$col",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        for (row in 1 until boardSize + 1) {
            Row(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ('A' + row - 1).toString(),
                        textAlign = TextAlign.Center
                    )
                }

                for (col in 1 until boardSize + 1) {
                    val cellState = boardState(row, col)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .border(1.dp, Color.Black, RectangleShape)
                            .background(cellState.getColor())
                            .then(
                                // Add clickability only if callback is provided
                                if (onCellClick != null || onCellLongClick != null) {
                                    Modifier.combinedClickable(
                                        onClick = { onCellClick?.invoke(row, col) },
                                        onLongClick = { onCellLongClick?.invoke(row, col) }
                                    )
                                } else {
                                    Modifier
                                }
                            )
                    )
                }
            }
        }
    }
}