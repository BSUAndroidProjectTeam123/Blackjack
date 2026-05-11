package com.example.blackjackapp

import org.junit.Assert.assertEquals
import org.junit.Test

class BlackjackLogicTest {

    private fun calculateScore(cards: List<String>): Int {
        var score = 0
        var aces = 0
        for (value in cards) {
            when (value) {
                "JACK", "QUEEN", "KING" -> score += 10
                "ACE" -> { aces += 1; score += 11 }
                else -> score += value.toInt()
            }
        }
        while (score > 21 && aces > 0) {
            score -= 10
            aces -= 1
        }
        return score
    }

    @Test
    fun testSimpleHandCalculation() {
        val cards = listOf("10", "7")
        assertEquals(17, calculateScore(cards))
    }

    @Test
    fun testFaceCardsCalculation() {
        val cards = listOf("KING", "JACK")
        assertEquals(20, calculateScore(cards))
    }

    @Test
    fun testAceAs11() {
        val cards = listOf("ACE", "9")
        assertEquals(20, calculateScore(cards))
    }

    @Test
    fun testAceAs1WhenOverflowing() {
        val cards = listOf("ACE", "JACK", "KING")
        assertEquals(21, calculateScore(cards))
    }
}