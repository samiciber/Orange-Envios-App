package com.example.logistiq.gestion

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.logistiq.databinding.FragmentDestinatarioBinding
import com.example.logistiq.models.PersonData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RecipientFragment : BaseFormFragment() {
    private var _binding: FragmentDestinatarioBinding? = null
    private val binding get() = _binding!!

    private var senderKey: String = ""
    private var productType: String = "sobre"
    private var price: Int = 8
    private var origenNombre: String = ""
    private var destinoNombre: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = RecipientFragmentArgs.fromBundle(requireArguments())
        senderKey = args.senderKey
        productType = args.productType
        price = args.price
        origenNombre = args.origenNombre
        destinoNombre = args.destinoNombre
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDestinatarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)  // ← INICIALIZA VIEWS Y SWITCHES

        // === AUTOCOMPLETAR CLIENTE AL ESCRIBIR DNI/RUC/CE ===
        etDni.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val doc = s.toString().trim()
                if (doc.length >= 8) {
                    searchClientInFirebase(doc, getDocumentType())
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // === BOTÓN REGISTRAR → SOBREESCRIBE LA LÓGICA DE BaseFormFragment ===
        btnRegister.setOnClickListener {
            saveToRealtimeDB()  // ← Llama a la versión sobrescrita
        }
    }

    // === BÚSQUEDA EN CLIENTES ===
    private fun searchClientInFirebase(doc: String, type: String) {
        database.child("clients").child(type).child(doc)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val data = snapshot.getValue(PersonData::class.java)
                        data?.let {
                            etName.setText(it.name)
                            etPaterno.setText(it.paterno)
                            etMaterno.setText(it.materno)
                            etPhone.setText(it.phone)
                            Toast.makeText(requireContext(), "Cliente encontrado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // === SOBREESCRIBE saveToRealtimeDB() PARA DESTINATARIO ===
    override fun saveToRealtimeDB() {
        val documentType = getDocumentType()
        val documentValue = etDni.text.toString().trim()

        if (documentValue.isEmpty()) {
            Toast.makeText(requireContext(), "Ingresa el documento", Toast.LENGTH_SHORT).show()
            return
        }

        val personData = PersonData(
            dni = documentValue,
            name = etName.text.toString().trim(),
            paterno = etPaterno.text.toString().trim(),
            materno = etMaterno.text.toString().trim(),
            phone = etPhone.text.toString().trim(),
            type = documentType,
            isSender = false,
            senderId = senderKey,
            productType = productType,
            price = price,
            origen = origenNombre,
            destino = destinoNombre
        )

        // === 1. GUARDAR EN CLIENTES (para búsquedas futuras) ===
        database.child("clients").child(documentType).child(documentValue)
            .setValue(personData.toMap())

        // === 2. GUARDAR COMO DESTINATARIO CON CLAVE ÚNICA ===
        val ref = database.child("recipients").push()
        val recipientKey = ref.key ?: return

        ref.setValue(personData.toMap().plus("recipientKey" to recipientKey))
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "¡Destinatario guardado! S/ $price.00",
                    Toast.LENGTH_SHORT
                ).show()

                clearForm()

                // === NAVEGAR A SECURITY PIN ===
                val action = RecipientFragmentDirections.actionRecipientToSecurityPin(
                    senderKey = senderKey,
                    recipientKey = recipientKey,
                    productType = productType,
                    price = price,
                    origen = origenNombre,
                    destino = destinoNombre
                )
                findNavController().navigate(action)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // === SOBREESCRIBE clearForm() SI NECESITAS MÁS ===
    override fun clearForm() {
        super.clearForm()  // ← Limpia todo de BaseFormFragment
        // Puedes agregar más lógica aquí si necesitas
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}