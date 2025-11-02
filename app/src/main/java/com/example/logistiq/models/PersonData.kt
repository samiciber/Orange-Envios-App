package com.example.logistiq.models

import com.google.firebase.database.PropertyName
import com.google.firebase.database.Exclude
data class PersonData(
    val dni: String = "",
    val name: String = "",
    val paterno: String = "",
    val materno: String = "",
    val phone: String = "",
    val type: String = "DNI",
    val isSender: Boolean = true,
    val senderId: String? = null,
    val productType: String = "",
    val price: Int = 0,
    val origen: String = "",
    val destino: String = ""
) {
    fun toMap(): Map<String, Any> = mapOf(
        "dni" to dni,
        "name" to name,
        "paterno" to paterno,
        "materno" to materno,
        "phone" to phone,
        "type" to type,
        "isSender" to isSender,
        "senderId" to (senderId ?: ""),
        "productType" to productType,
        "price" to price,
        "origen" to origen,
        "destino" to destino
    )
}