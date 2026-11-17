package com.example.battleshipsmobilegame.logic

import com.example.battleshipsmobilegame.CellState

class GameLogic {
    private val BOARD_SIZE = 10
    private val ALLOWED_FLEET = mapOf(1 to 4, 2 to 3, 3 to 2, 4 to 1)

    // PLACING SHIPS

    fun toggleShipPlacement(board: List<List<CellState>>, row: Int, col: Int): List<List<CellState>> {
        val r = row - 1
        val c = col - 1
        if (!isInBounds(r, c)) return board

        val currentState = board[r][c]

        // Erasing ship
        if (currentState == CellState.SHIP) {
            return updateBoard(board, r, c, CellState.WATER)
        }

        // placing ship
        if (currentState == CellState.WATER) {
            if (isValidPlacement(board, r, c)) {
                return updateBoard(board, r, c, CellState.SHIP)
            }
        }
        return board
    }

    private fun isValidPlacement(board: List<List<CellState>>, r: Int, c: Int): Boolean {
        // checking corners
        val diagonals = listOf(Pair(-1, -1), Pair(-1, 1), Pair(1, -1), Pair(1, 1))
        for (d in diagonals) {
            val nr = r + d.first
            val nc = c + d.second
            if (isInBounds(nr, nc) && board[nr][nc] == CellState.SHIP) {
                return false
            }
        }

        val tempBoard = updateBoard(board, r, c, CellState.SHIP)
        val shipCells = getConnectedShipCells(tempBoard, r, c)

        // Avoiding L shapes
        val isHorizontal = shipCells.all { it.first == shipCells.first().first }
        val isVertical = shipCells.all { it.second == shipCells.first().second }
        if (!isHorizontal && !isVertical) return false

        // checking max-length
        if (shipCells.size > 4) return false

        // checking if all ships are placed
        return isFleetValid(tempBoard)
    }

    private fun isFleetValid(board: List<List<CellState>>): Boolean {
        val visited = mutableSetOf<Pair<Int, Int>>()
        val fleetCount = mutableMapOf<Int, Int>().withDefault { 0 }

        // Counting ships on board
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                if (board[i][j] == CellState.SHIP && Pair(i, j) !in visited) {
                    val ship = getConnectedShipCells(board, i, j)
                    visited.addAll(ship)
                    val length = ship.size
                    if (length > 4) return false // Zabezpieczenie przed 5-masztowcami
                    fleetCount[length] = fleetCount.getValue(length) + 1
                }
            }
        }

        var availableSlotsFromLargerShips = 0

        for (length in 4 downTo 1) {
            val allowedForThisLength = ALLOWED_FLEET[length] ?: 0
            val currentCount = fleetCount.getValue(length)

            val maxAllowedNow = allowedForThisLength + availableSlotsFromLargerShips

            if (currentCount > maxAllowedNow) {
                return false
            }

            availableSlotsFromLargerShips = maxAllowedNow - currentCount
        }

        return true
    }

    fun isFleetComplete(board: List<List<CellState>>): Boolean {
        val visited = mutableSetOf<Pair<Int, Int>>()
        val fleetCount = mutableMapOf<Int, Int>().withDefault { 0 }

        for (r in 0 until BOARD_SIZE) {
            for (c in 0 until BOARD_SIZE) {
                if (board[r][c] == CellState.SHIP && Pair(r, c) !in visited) {
                    val ship = getConnectedShipCells(board, r, c)
                    visited.addAll(ship)
                    val length = ship.size
                    fleetCount[length] = fleetCount.getValue(length) + 1
                }
            }
        }

        for ((length, requiredCount) in ALLOWED_FLEET) {
            val currentCount = fleetCount.getValue(length)
            if (currentCount != requiredCount) {
                return false
            }
        }
        return true
    }

    // ATTACKING AND PREDICTIONS

    fun togglePrediction(targetBoard: List<List<CellState>>, row: Int, col: Int): List<List<CellState>> {
        val r = row - 1
        val c = col - 1
        if (!isInBounds(r, c)) return targetBoard

        return when (targetBoard[r][c]) {
            CellState.UNKNOWN -> updateBoard(targetBoard, r, c, CellState.PREDICTION)
            CellState.PREDICTION -> updateBoard(targetBoard, r, c, CellState.UNKNOWN)
            else -> targetBoard
        }
    }

    fun attack(
        attackerTargetBoard: List<List<CellState>>,
        defenderBoard: List<List<CellState>>,
        row: Int, col: Int
    ): Pair<List<List<CellState>>, List<List<CellState>>> {
        val r = row - 1
        val c = col - 1
        if (!isInBounds(r, c)) return Pair(attackerTargetBoard, defenderBoard)

        if (attackerTargetBoard[r][c] != CellState.UNKNOWN) {
            return Pair(attackerTargetBoard, defenderBoard)
        }

        var newTarget = attackerTargetBoard
        var newDefender = defenderBoard

        if (defenderBoard[r][c] == CellState.SHIP) {
            newTarget = updateBoard(newTarget, r, c, CellState.HIT)
            newDefender = updateBoard(newDefender, r, c, CellState.HIT)

            if (isShipSunk(newDefender, r, c)) {
                val shipCells = getConnectedShipCells(newDefender, r, c, listOf(CellState.HIT, CellState.SHIP))
                for (cell in shipCells) {
                    newTarget = updateBoard(newTarget, cell.first, cell.second, CellState.SUNK)
                    newDefender = updateBoard(newDefender, cell.first, cell.second, CellState.SUNK)
                }
            }
        } else if (defenderBoard[r][c] == CellState.WATER) {
            newTarget = updateBoard(newTarget, r, c, CellState.MISS)
            newDefender = updateBoard(newDefender, r, c, CellState.MISS)
        }

        return Pair(newTarget, newDefender)
    }

    private fun isShipSunk(board: List<List<CellState>>, r: Int, c: Int): Boolean {
        val shipCells = getConnectedShipCells(board, r, c, listOf(CellState.HIT, CellState.SHIP))
        return shipCells.none { board[it.first][it.second] == CellState.SHIP }
    }

    // TECHNICAL FUNCTIONS

    private fun updateBoard(board: List<List<CellState>>, r: Int, c: Int, newState: CellState): List<List<CellState>> {
        val newBoard = board.map { it.toMutableList() }.toMutableList()
        newBoard[r][c] = newState
        return newBoard
    }

    private fun isInBounds(r: Int, c: Int): Boolean {
        return r in 0 until BOARD_SIZE && c in 0 until BOARD_SIZE
    }

    private fun getConnectedShipCells(
        board: List<List<CellState>>,
        startR: Int,
        startC: Int,
        targetStates: List<CellState> = listOf(CellState.SHIP)
    ): List<Pair<Int, Int>> {
        val cells = mutableSetOf<Pair<Int, Int>>()
        val queue = mutableListOf(Pair(startR, startC))
        val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))

        while (queue.isNotEmpty()) {
            val curr = queue.removeAt(0)
            if (cells.add(curr)) {
                for (dir in directions) {
                    val nr = curr.first + dir.first
                    val nc = curr.second + dir.second
                    if (isInBounds(nr, nc) && board[nr][nc] in targetStates && Pair(nr, nc) !in cells) {
                        queue.add(Pair(nr, nc))
                    }
                }
            }
        }
        return cells.toList()
    }
}