package com.example.blackjackapp

import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick

class BlackjackUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testMenuButtonsExist() {
        // Проверяем, что на главном экране есть кнопки
        composeTestRule.onNodeWithText("Новая игра").assertExists()
        composeTestRule.onNodeWithText("История игр").assertExists()
    }

    @Test
    fun testNavigationToGame() {
        // Проверяем переход в игру
        composeTestRule.onNodeWithText("Новая игра").performClick()

        // Проверяем, что на игровом экране появились кнопки управления
        composeTestRule.onNodeWithText("Hit (Взять)").assertExists()
        composeTestRule.onNodeWithText("Stand (Хватит)").assertExists()
    }
}