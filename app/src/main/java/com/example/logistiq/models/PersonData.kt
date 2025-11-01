package com.example.logistiq.models
import com.google.firebase.database.PropertyName

data class PersonData(
    @PropertyName("dni") val dni: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("paterno") val paterno: String = "",
    @PropertyName("materno") val materno: String = "",
    @PropertyName("phone") val phone: String = "",
    @PropertyName("type") val type: String = "DNI",  // DNI, RUC, CE
    @PropertyName("createdAt") val createdAt: Long = System.currentTimeMillis(),
    @PropertyName("isSender") val isSender: Boolean = false  // Para distinguir remitente/destinatario
)
