package com.example.logistiq.gestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.logistiq.databinding.FragmentSecurityPinBinding
import com.example.logistiq.models.EnvioData
import com.example.logistiq.models.PersonData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class SecurityPinFragment : Fragment() {
    private var _binding: FragmentSecurityPinBinding? = null
    private val binding get() = _binding!!

    private var pin = ""

    // DATOS RECIBIDOS
    private var senderKey: String = ""
    private var recipientKey: String = ""
    private var productType: String = "sobre"
    private var price: Int = 8
    private var origen: String = ""
    private var destino: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = SecurityPinFragmentArgs.fromBundle(requireArguments())
        senderKey = args.senderKey
        recipientKey = args.recipientKey
        productType = args.productType
        price = args.price
        origen = args.origen
        destino = args.destino
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecurityPinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPinPad()

        binding.btnCreateOrder.setOnClickListener {
            if (validatePin()) {
                val orderCode = generateOrderCode()
                saveToFirebase(orderCode, pin)
            }
        }
    }

    private fun setupPinPad() {
        val buttons = listOf(
            binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6,
            binding.btn7, binding.btn8, binding.btn9,
            binding.btn0
        )

        buttons.forEach { btn ->
            btn.setOnClickListener {
                if (pin.length < 4) {
                    pin += btn.text
                    updatePinDots()
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            if (pin.isNotEmpty()) {
                pin = pin.dropLast(1)
                updatePinDots()
            }
        }
    }

    private fun updatePinDots() {
        val filled = "•".repeat(pin.length)
        val empty = "○".repeat(4 - pin.length)
        binding.pinDots.text = "$filled $empty"
    }

    private fun validatePin(): Boolean {
        return if (pin.length == 4) {
            true
        } else {
            Toast.makeText(requireContext(), "Ingresa 4 dígitos", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun generateOrderCode(): String {
        val prefix = (60000000..69999999).random()
        val suffix = (100..999).random()
        return "$prefix$suffix" // Ej: 612345678901
    }

    private fun saveToFirebase(orderCode: String, pin: String) {
        val database = Firebase.database.reference
        val envioRef = database.child("envios").child(orderCode)

        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        // CARGAR REMITENTE
        database.child("senders").child(senderKey).get()
            .addOnSuccessListener { senderSnap ->
                val sender = senderSnap.getValue(PersonData::class.java)

                // CARGAR DESTINATARIO
                database.child("recipients").child(recipientKey).get()
                    .addOnSuccessListener { recipientSnap ->
                        val recipient = recipientSnap.getValue(PersonData::class.java)

                        val envio = EnvioData(
                            orderCode = orderCode,
                            pin = pin,
                            fecha = fecha,
                            productType = productType,
                            price = price,
                            origen = origen,
                            destino = destino,
                            sender = sender,
                            recipient = recipient
                        )

                        envioRef.setValue(envio.toMap())
                            .addOnSuccessListener {
                                val action = SecurityPinFragmentDirections
                                    .actionSecurityPinToThanks(orderCode = orderCode)
                                findNavController().navigate(action)
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Error al guardar envío", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Error al cargar destinatario", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar remitente", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
