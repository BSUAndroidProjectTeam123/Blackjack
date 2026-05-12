package com.example.blackjackapp // Замени на свой пакет, если отличается

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blackjackapp.ui.BlackjackViewModel
import com.example.blackjackapp.ui.GameScreen
import com.example.blackjackapp.ui.HistoryScreen
import com.example.blackjackapp.ui.MenuScreen
import com.example.blackjackapp.ui.theme.BlackjackAppTheme
// Сюда позже добавятся импорты твоих экранов: MenuScreen, GameScreen, HistoryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlackjackAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Контроллер для управления переходами
                    val navController = rememberNavController()
                    // Наша ViewModel, которая будет жить пока жива Activity
                    val viewModel: BlackjackViewModel = viewModel()

                    // Многооконный интерфейс
                    NavHost(navController = navController, startDestination = "menu") {
                        composable("menu") {
                            MenuScreen(navController, viewModel)
                        }
                        composable("game") {
                            GameScreen(viewModel)
                        }
                        composable("history") {
                            HistoryScreen(viewModel, navController)
                        }
                    }
                }
            }
        }
    }
}