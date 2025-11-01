package com.example.logistiq.gestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.logistiq.R
import com.example.logistiq.models.PersonData
import androidx.navigation.fragment.findNavController

class RecipientFragment : BaseFormFragment() {
    private var senderKey: String? = null
    private var productType: String = "sobre"
    private var price: Int = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        senderKey = arguments?.getString("senderKey")
        productType = arguments?.getString("productType") ?: "sobre"
        price = arguments?.getInt("price") ?: 8
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
            senderId = senderKey,
            productType = productType,  // RECIBIDO DEL REMITENTE
            price = price               // RECIBIDO DEL REMITENTE
        )

        val ref = database.child("recipients").push()

        ref.setValue(personData.toMap())
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "¡Envío registrado! Total: S/ $price.00",
                    Toast.LENGTH_LONG
                ).show()
                clearForm()

                // VOLVER A TIPO DE PRODUCTO (limpia el stack)
                findNavController().navigate(R.id.action_recipient_to_inicio)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}