package com.example.blackjackapp.data.remote
data class DeckResponse(
    val success: Boolean,
    val deck_id: String,
    val shuffled: Boolean,
    val remaining: Int
)

data class DrawResponse(
    val success: Boolean,
    val deck_id: String,
    val cards: List<CardInfo>,
    val remaining: Int
)

data class CardInfo(
    val image: String,
    val value: String, // "2", "10", "JACK", "QUEEN", "KING", "ACE"
    val suit: String,
    val code: String
)