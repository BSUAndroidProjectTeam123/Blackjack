package com.example.blackjackapp.data.remote
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeckApiService {
    // Получить новую перемешанную колоду (1 колода)
    @GET("api/deck/new/shuffle/?deck_count=1")
    suspend fun createNewDeck(): DeckResponse

    // Взять карту из конкретной колоды
    @GET("api/deck/{deck_id}/draw/")
    suspend fun drawCards(
        @Path("deck_id") deckId: String,
        @Query("count") count: Int
    ): DrawResponse
}

// Объект-синглтон для создания Retrofit
object RetrofitClient {
    private const val BASE_URL = "https://deckofcardsapi.com/"

    val apiService: DeckApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeckApiService::class.java)
    }
}