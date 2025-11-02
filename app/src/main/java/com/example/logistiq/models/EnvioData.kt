package com.example.logistiq.models
import com.google.firebase.database.Exclude

data class EnvioData(
    val orderCode: String = "",
    val pin: String = "",
    val fecha: String = "",
    val productType: String = "",
    val price: Int = 0,
    val origen: String = "",
    val destino: String = "",
    val sender: PersonData? = null,
    val recipient: PersonData? = null
) {
    fun toMap(): Map<String, Any> = mapOf(
        "orderCode" to orderCode,
        "pin" to pin,
        "fecha" to fecha,
        "productType" to productType,
        "price" to price,
        "origen" to origen,
        "destino" to destino,
        "sender" to (sender?.toMap() ?: ""),
        "recipient" to (recipient?.toMap() ?: "")
    )
}