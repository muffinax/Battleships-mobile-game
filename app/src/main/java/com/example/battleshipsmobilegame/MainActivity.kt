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

import com.example.battleshipsmobilegame.screens.GameParametersScreen
import com.example.battleshipsmobilegame.screens.MainMenuScreen
import com.example.battleshipsmobilegame.screens.SetupTurnScreen
import com.example.battleshipsmobilegame.screens.SetupBattleShipsScreen
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
                                    navController.navigate("setup_turn")
                                }
                            )
                        }

                        // Setup Turn
                        composable("setup_turn") {
                            SetupTurnScreen(
                                player = player1Name,
                                onReadyClick = {
                                    navController.navigate("board")
                                }
                            )
                        }

                        // The Board for UI tests
                        composable("board") {
                            var dummyBoard by remember {
                                mutableStateOf(List(10) { List(10) { CellState.WATER } })
                            }

                            SetupBattleShipsScreen(
                                player = player1Name,
                                boardState = { row, col ->
                                    dummyBoard[row - 1][col - 1]
                                },
                                onCellClick = { row, col ->
                                    // wizual tests
                                    val r = row - 1
                                    val c = col - 1
                                    val newBoard = dummyBoard.map { it.toMutableList() }.toMutableList()

                                    if (newBoard[r][c] == CellState.WATER) {
                                        newBoard[r][c] = CellState.SHIP
                                    } else {
                                        newBoard[r][c] = CellState.WATER
                                    }

                                    dummyBoard = newBoard
                                },
                                onReadyClick = {
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