package com.example.logistiq

import com.google.ai.client.generativeai.GenerativeModel

class GeminiApiClient {

    private val generativeModel = GenerativeModel(
        modelName = MODEL_NAME,
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun generateContent(prompt: String): String {
        val response = generativeModel.generateContent(prompt)
        return response.text ?: "Error: La respuesta de Gemini no contenía texto."
    }

    companion object {
        private const val MODEL_NAME = "gemini-1.5-flash"
    }
}