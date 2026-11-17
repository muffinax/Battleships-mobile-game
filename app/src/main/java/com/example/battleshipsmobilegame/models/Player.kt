package com.example.battleshipsmobilegame.models

import com.example.battleshipsmobilegame.CellState

data class Player(
    val id: Int,
    val name: String,
    val ownBoard: List<List<CellState>> = List(10) { List(10) { CellState.WATER } },
    val targetBoard: List<List<CellState>> = List(10) { List(10) { CellState.UNKNOWN } }
)