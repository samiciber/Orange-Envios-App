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
    private var productType: String = "sobre"
    private var price: Int = 8 // precio por defecto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productType = arguments?.getString("productType") ?: "sobre"

        // CALCULAR PRECIO
        price = when (productType) {
            "sobre" -> 8
            "caja_pequena" -> 15
            "caja_mediana" -> 30
            "caja_grande" -> 40
            "otra_medida" -> 60
            else -> 8
        }
    }

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
            isSender = true,
            productType = productType,
            price = price  // GUARDAR PRECIO
        )

        val ref = database.child("senders").push()
        val senderKey = ref.key ?: return

        ref.setValue(personData.toMap())
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "Remitente guardado - S/ $price.00",
                    Toast.LENGTH_LONG
                ).show()
                clearForm()

                val bundle = Bundle().apply {
                    putString("senderKey", senderKey)
                    putString("productType", productType)
                    putInt("price", price) // PASAR AL DESTINATARIO
                }
                findNavController().navigate(
                    R.id.action_sender_to_recipient,
                    bundle
                )
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}