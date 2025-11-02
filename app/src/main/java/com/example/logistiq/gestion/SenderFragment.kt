package com.example.logistiq.gestion

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.logistiq.models.PersonData

class SenderFragment : BaseFormFragment() {
    private var productType: String = "sobre"
    private var price: Int = 8
    private var origenNombre: String = ""
    private var destinoNombre: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = SenderFragmentArgs.fromBundle(requireArguments())
        productType = args.productType
        price = args.price
        origenNombre = args.origenNombre
        destinoNombre = args.destinoNombre
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        btnRegister.setOnClickListener { saveToRealtimeDB() }
    }

    private fun searchClientInFirebase(doc: String, type: String) {
        database.child("clients").child(type).child(doc).get()
            .addOnSuccessListener { snap ->
                if (snap.exists()) {
                    val data = snap.getValue(PersonData::class.java)
                    data?.let {
                        etName.setText(it.name)
                        etPaterno.setText(it.paterno)
                        etMaterno.setText(it.materno)
                        etPhone.setText(it.phone)
                    }
                }
            }
    }

    override fun saveToRealtimeDB() {
        val doc = etDni.text.toString().trim()
        if (doc.isEmpty()) return

        val person = PersonData(
            dni = doc,
            name = etName.text.toString().trim(),
            paterno = etPaterno.text.toString().trim(),
            materno = etMaterno.text.toString().trim(),
            phone = etPhone.text.toString().trim(),
            type = getDocumentType(),
            isSender = true,
            productType = productType,
            price = price,
            origen = origenNombre,
            destino = destinoNombre
        )

        database.child("clients").child(person.type).child(person.dni).setValue(person.toMap())

        val ref = database.child("senders").push()
        val senderKey = ref.key ?: return

        ref.setValue(person.toMap())
            .addOnSuccessListener {
                clearForm()
                val action = SenderFragmentDirections.actionSenderToRecipient(
                    senderKey, productType, price, origenNombre, destinoNombre
                )
                findNavController().navigate(action)
            }
    }
}