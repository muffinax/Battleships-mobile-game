package com.example.battleshipsmobilegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.battleshipsmobilegame.logic.GameLogic
import com.example.battleshipsmobilegame.logic.ShotType
import com.example.battleshipsmobilegame.screens.ActionScreen
import com.example.battleshipsmobilegame.screens.ChangeTurnScreen
import com.example.battleshipsmobilegame.screens.GameParametersScreen
import com.example.battleshipsmobilegame.screens.MainMenuScreen
import com.example.battleshipsmobilegame.screens.SetupTurnScreen
import com.example.battleshipsmobilegame.screens.SetupBattleShipsScreen
import com.example.battleshipsmobilegame.screens.SetupTurnScreen
import com.example.battleshipsmobilegame.screens.WinnerScreen
import com.example.battleshipsmobilegame.ui.theme.BattleshipsMobileGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BattleshipsMobileGameTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val hasSavedGame by remember { mutableStateOf(false) }
                    var player1Name by remember { mutableStateOf("Player 1") }
                    var player2Name by remember { mutableStateOf("Player 2") }

                    val gameLogic = remember { GameLogic() }

                    // Plansze
                    var p1OwnBoard by remember { mutableStateOf(List(10) { List(10) { CellState.WATER } }) }
                    var p2OwnBoard by remember { mutableStateOf(List(10) { List(10) { CellState.WATER } }) }
                    var p1TargetBoard by remember { mutableStateOf(List(10) { List(10) { CellState.UNKNOWN } }) }
                    var p2TargetBoard by remember { mutableStateOf(List(10) { List(10) { CellState.UNKNOWN } }) }

                    // Stan tury (Bitwa)
                    var isPlayer1Turn by remember { mutableStateOf(true) }
                    var hitsInCurrentTurn by remember { mutableStateOf(0) }
                    var turnResultMessage by remember { mutableStateOf("") }

                    var canShoot by remember { mutableStateOf(true) }
                    var shotResultText by remember { mutableStateOf("") }
                    var winnerName by remember { mutableStateOf("") }

                    val resetGame = {
                        p1OwnBoard = List(10) { List(10) { CellState.WATER } }
                        p2OwnBoard = List(10) { List(10) { CellState.WATER } }
                        p1TargetBoard = List(10) { List(10) { CellState.UNKNOWN } }
                        p2TargetBoard = List(10) { List(10) { CellState.UNKNOWN } }
                        isPlayer1Turn = true
                        hitsInCurrentTurn = 0
                        turnResultMessage = ""
                        canShoot = true
                        shotResultText = ""
                        winnerName = ""
                    }

                    NavHost(
                        navController = navController,
                        startDestination = "main_menu",
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        // Main Menu
                        composable("main_menu") {
                            MainMenuScreen(
                                hasSavedGame = hasSavedGame,
                                onNewGameClick = {
                                    resetGame()
                                    navController.navigate("parameters")
                                }
                            )
                        }

                        // Game Parameters
                        composable("parameters") {
                            GameParametersScreen(
                                player1Name = player1Name,
                                onPlayer1NameChange = { player1Name = it },
                                player2Name = player2Name,
                                onPlayer2NameChange = { player2Name = it },
                                onPlayClick = {
                                    navController.navigate("transition_setup_p1") {
                                        popUpTo("parameters") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // Układanie statków Gracza 1
                        composable("transition_setup_p1") {
                            SetupTurnScreen(
                                player = player1Name,
                                onReadyClick = {
                                    navController.navigate("setup_p1") {
                                        popUpTo("transition_setup_p1") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("setup_p1") {
                            val isFleetReady = gameLogic.isFleetComplete(p1OwnBoard)

                            SetupBattleShipsScreen(
                                player = player1Name,
                                boardState = { row, col -> p1OwnBoard[row - 1][col - 1] },
                                onCellClick = { row, col ->
                                    p1OwnBoard = gameLogic.toggleShipPlacement(p1OwnBoard, row, col)
                                },
                                isConfirmEnabled = isFleetReady,
                                onReadyClick = {
                                    navController.navigate("transition_setup_p2") {
                                        popUpTo("setup_p1") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // Układanie statków Gracza 2
                        composable("transition_setup_p2") {
                            SetupTurnScreen(
                                player = player2Name,
                                onReadyClick = {
                                    navController.navigate("setup_p2") {
                                        popUpTo("transition_setup_p2") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("setup_p2") {
                            val isFleetReady = gameLogic.isFleetComplete(p2OwnBoard)

                            SetupBattleShipsScreen(
                                player = player2Name,
                                boardState = { row, col -> p2OwnBoard[row - 1][col - 1] },
                                onCellClick = { row, col ->
                                    p2OwnBoard = gameLogic.toggleShipPlacement(p2OwnBoard, row, col)
                                },
                                isConfirmEnabled = isFleetReady,
                                onReadyClick = {
                                    navController.navigate("transition_combat") {
                                        popUpTo("main_menu") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // Następna tura gracza
                        composable("transition_combat") {
                            val nextPlayer = if (isPlayer1Turn) player1Name else player2Name
                            ChangeTurnScreen(
                                player = nextPlayer,
                                result = turnResultMessage,
                                onReadyClick = {
                                    navController.navigate("action_screen") {
                                        popUpTo("transition_combat") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // Tura atakowania
                        composable("action_screen") {
                            val currentPlayerName = if (isPlayer1Turn) player1Name else player2Name
                            val ownBoard = if (isPlayer1Turn) p1OwnBoard else p2OwnBoard
                            val targetBoard = if (isPlayer1Turn) p1TargetBoard else p2TargetBoard
                            val defenderBoard = if (isPlayer1Turn) p2OwnBoard else p1OwnBoard
//                            val remainingEnemyShipsText = gameLogic.getRemainingEnemyShipsText(defenderBoard)

                            ActionScreen(
                                player = currentPlayerName,
                                ownBoardState = { r, c -> ownBoard[r - 1][c - 1] },
                                targetBoardState = { r, c -> targetBoard[r - 1][c - 1] },
                                remainingEnemyShipsText = "Not implememnted",

                                //Prediction
                                onTargetCellLongClick = { r, c ->
                                    val newTargetBoard = gameLogic.togglePrediction(targetBoard, r, c)
                                    if (isPlayer1Turn) p1TargetBoard = newTargetBoard else p2TargetBoard = newTargetBoard
                                },

                                //Attack
                                onTargetCellClick = { r, c ->
                                    val attackerTargetBoard = if (isPlayer1Turn) p1TargetBoard else p2TargetBoard
                                    val defenderBoard = if (isPlayer1Turn) p2OwnBoard else p1OwnBoard

                                    val result = gameLogic.attack(attackerTargetBoard, defenderBoard, r, c)

                                    if (isPlayer1Turn) {
                                        p1TargetBoard = result.newTargetBoard
                                        p2OwnBoard = result.newDefenderBoard
                                    } else {
                                        p2TargetBoard = result.newTargetBoard
                                        p1OwnBoard = result.newDefenderBoard
                                    }

                                    val currentDefenderBoard = if (isPlayer1Turn) p2OwnBoard else p1OwnBoard

                                    // if its last (winning) shot
                                    if (gameLogic.isGameOver(currentDefenderBoard)) {
                                        winnerName = currentPlayerName
                                        navController.navigate("winner") {
                                            popUpTo("action_screen") { inclusive = true }
                                        }
                                    } else {
                                        // if hit an enemy
                                        when (result.shotType) {
                                            ShotType.HIT, ShotType.SUNK -> {
                                                shotResultText = if (result.shotType == ShotType.SUNK) "Sunk!" else "Hit!"
                                                hitsInCurrentTurn++
                                            }
                                            //stops the loop when missed a shot
                                            ShotType.MISS -> {
                                                shotResultText = "Miss!"
                                                canShoot = false
                                            }
                                            ShotType.IGNORED -> {}
                                        }
                                    }
                                },

                                onEndTurnClick = {
                                    turnResultMessage = if (hitsInCurrentTurn > 0) {
                                        "Your opponent hit $hitsInCurrentTurn of your ship cells!"
                                    } else {
                                        "Your opponent missed completely!"
                                    }

                                    hitsInCurrentTurn = 0
                                    canShoot = true
                                    shotResultText = ""
                                    isPlayer1Turn = !isPlayer1Turn

                                    navController.navigate("transition_combat") {
                                        popUpTo("action_screen") { inclusive = true }
                                    }
                                },
                                shotResultText = shotResultText,
                                canShoot = canShoot
                            )
                        }

                        // Ekran wygrania
                        composable("winner") {
                            WinnerScreen(
                                player = winnerName,
                                onPlayAgainClick = {
                                    resetGame()
                                    navController.navigate("parameters") {
                                        popUpTo("main_menu") { inclusive = false }
                                    }
                                },
                                onMenuClick = {
                                    resetGame()
                                    navController.popBackStack("main_menu", inclusive = false)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}