package com.example.logistiq.gestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.logistiq.R
import com.example.logistiq.models.PersonData
import com.google.firebase.database.FirebaseDatabase

class SenderFragment : BaseFormFragment() {
    private var senderKey: String? = null  // ← Guarda el ID del remitente

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_remitente, container, false)
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
            isSender = true
        )

        val ref = database.child("senders").push()
        senderKey = ref.key  // ← Guarda el ID para el destinatario

        ref.setValue(personData.toMap())
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Remitente guardado", Toast.LENGTH_SHORT).show()
                clearForm()
                // Pasar al siguiente fragment
                // AHORA (con Bundle para pasar el ID)
                val bundle = Bundle().apply {
                    putString("senderKey", senderKey)
                }
                findNavController().navigate(R.id.action_sender_to_recipient, bundle)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al guardar remitente", Toast.LENGTH_SHORT).show()
            }
    }

    // Método para pasar el ID al destinatario
    fun getSenderKey(): String? = senderKey

}