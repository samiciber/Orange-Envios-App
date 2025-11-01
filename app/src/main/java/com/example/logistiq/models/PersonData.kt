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
    val createdAt: Long = System.currentTimeMillis(),
    val isSender: Boolean = false,
    val senderId: String? = null,
    val productType: String = "sobre",
    val price: Int = 8
) {
    @Exclude
    fun toMap(): Map<String, Any?> = mapOf(
        "dni" to dni,
        "name" to name,
        "paterno" to paterno,
        "materno" to materno,
        "phone" to phone,
        "type" to type,
        "createdAt" to createdAt,
        "isSender" to isSender,
        "senderId" to senderId,
        "productType" to productType,
        "price" to price
    )
}
