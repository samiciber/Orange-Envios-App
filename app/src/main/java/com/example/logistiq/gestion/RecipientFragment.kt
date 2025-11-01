package com.example.logistiq.gestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.logistiq.R
import com.example.logistiq.models.PersonData

class RecipientFragment : BaseFormFragment() {
    private var senderKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        senderKey = arguments?.getString("senderKey")  // ← MEJOR AQUÍ
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_destinatario, container, false)
    }

    override fun saveToRealtimeDB() {
        val documentType = getDocumentType()
        val documentValue = etDni.text.toString().trim()

        val personData = PersonData(
            dni = documentValue,
            name = etName.text.toString().trim(),
            paterno = etPaterno.text.toString().trim(),
            materno = etMaterno.text.toString().trim(),
            phone = etPhone.text.toString().trim(),
            type = documentType,
            isSender = false,
            senderId = senderKey  // ← Perfecto
        )

        val ref = database.child("recipients").push()

        ref.setValue(personData.toMap())
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "¡Destinatario guardado con éxito!", Toast.LENGTH_SHORT).show()
                clearForm()
                // ¡ENVÍO COMPLETO! Puedes ir a "Resumen de Envío"
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}