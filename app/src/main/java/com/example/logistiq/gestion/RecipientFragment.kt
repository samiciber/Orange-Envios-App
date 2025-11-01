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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Recuperar el ID del remitente desde argumentos
        senderKey = arguments?.getString("senderKey")
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
            senderId = senderKey  // ← Relaciona con el remitente
        )

        val ref = database.child("recipients").push()

        ref.setValue(personData.toMap())
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Destinatario guardado", Toast.LENGTH_SHORT).show()
                clearForm()
                // Opcional: ir a pantalla de envío
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al guardar destinatario", Toast.LENGTH_SHORT).show()
            }
    }
}