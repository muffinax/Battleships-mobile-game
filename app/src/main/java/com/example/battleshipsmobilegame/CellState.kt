package com.example.battleshipsmobilegame

import androidx.compose.ui.graphics.Color

enum class CellState {
    WATER,   // Empty "water"
    SHIP,    // Placed ship
    HIT,     // Successfully hit ship
    SUNK,    //Successfully sunk ship
    MISS,    // Missed shot
    UNKNOWN, // Undiscovered opponent's cell
    PREDICTION; // Suspecting that place being a miss by a player

    // Helper function to map state to a specific UI color
    fun getColor(): Color = when (this) {
        WATER -> Color.Blue
        SHIP -> Color.Black
        HIT -> Color.Red
        SUNK -> Color.Magenta
        MISS -> Color.LightGray
        UNKNOWN -> Color.Gray
        PREDICTION -> Color.DarkGray
    }
}