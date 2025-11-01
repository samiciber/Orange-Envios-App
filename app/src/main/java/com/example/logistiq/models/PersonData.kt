package com.example.logistiq.models

import com.google.firebase.database.PropertyName
import com.google.firebase.database.Exclude
data class PersonData(
    @PropertyName("dni") val dni: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("paterno") val paterno: String = "",
    @PropertyName("materno") val materno: String = "",
    @PropertyName("phone") val phone: String = "",
    @PropertyName("type") val type: String = "DNI",
    @PropertyName("createdAt") val createdAt: Long = System.currentTimeMillis(),
    @PropertyName("isSender") val isSender: Boolean = false
) {
    // Constructor vacío para Firebase
    constructor() : this("", "", "", "", "", "DNI", System.currentTimeMillis(), false)

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "dni" to dni,
            "name" to name,
            "paterno" to paterno,
            "materno" to materno,
            "phone" to phone,
            "type" to type,
            "createdAt" to createdAt,
            "isSender" to isSender
        )
    }
}
