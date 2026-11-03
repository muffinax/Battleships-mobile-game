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
//import com.example.battleshipsmobilegame.screens.BattleshipBoard
import com.example.battleshipsmobilegame.screens.GameParametersScreen
import com.example.battleshipsmobilegame.screens.MainMenuScreen
import com.example.battleshipsmobilegame.screens.SetupTurnScreen
import com.example.battleshipsmobilegame.ui.theme.BattleshipsMobileGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BattleshipsMobileGameTheme {
                // Navigation controller
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val hasSavedGame by remember { mutableStateOf(false) }
                    var player1Name by remember { mutableStateOf("Player 1") }
                    var player2Name by remember { mutableStateOf("Player 2") }

                    // Navigation map
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
                                    // 1. "Play" navigates to the setup screen
                                    navController.navigate("setup_turn")
                                }
                            )
                        }

                        // Setup
                        composable("setup_turn") {
                            SetupTurnScreen(
                                player = player1Name, // In the future, this will change dynamically between Player 1 and 2
                                onReadyClick = {
                                    // 2. "Ready" navigates to the board
                                    navController.navigate("board")
                                }
                            )
                        }

                        // The Board
//                        composable("board") {
//                            BattleshipBoard()
//                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BoardPreview() {
    BattleshipsMobileGameTheme {
//        BattleshipBoard()
    }
}