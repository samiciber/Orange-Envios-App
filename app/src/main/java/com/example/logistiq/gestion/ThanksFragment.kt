package com.example.logistiq.gestion

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.logistiq.R
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.logistiq.databinding.FragmentThanksBinding
import com.example.logistiq.models.EnvioData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.java

class ThanksFragment: Fragment() {
    private var _binding: FragmentThanksBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderCode: String
    private var origen: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThanksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = ThanksFragmentArgs.fromBundle(requireArguments())
        orderCode = args.orderCode
        val orderNumber = orderCode.substring(0, 8)
        val code = orderCode.substring(8, 11)

        binding.tvOrderNumber.text = "N° de orden $orderNumber | Código $code"

        // === CARGAR ORIGEN DESDE FIREBASE ===
        Firebase.database.reference.child("envios").child(orderCode).get()
            .addOnSuccessListener { snap ->
                val envio = snap.getValue(EnvioData::class.java) ?: return@addOnSuccessListener
                origen = envio.origen ?: "Agencia"
                updateWarningText(origen)
            }

        // === AVISO FLOTANTE ===
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("¡Orden de servicio registrada!")
            .setMessage("Tu envío ha sido creado exitosamente.")
            .setPositiveButton("OK") { _, _ -> }
            .setCancelable(false)
            .show()

        // === BOTONES ===
        binding.btnDownload.setOnClickListener { downloadReceipt() }
        binding.btnViewDetails.setOnClickListener {
            val action = ThanksFragmentDirections.actionThanksToDetail(orderCode)
            findNavController().navigate(action)
        }
        binding.btnNewOrder.setOnClickListener {
            findNavController().navigate(R.id.action_thanks_to_tipoProducto)
        }
    }

    private fun updateWarningText(origen: String) {
        binding.tvWarning.text = """
            *Recuerda que tienes 24 hrs. para dejar tu envío en la agencia de:
            $origen
            pasado ese tiempo, tu envío será anulado automáticamente.
        """.trimIndent()
    }

    private fun downloadReceipt() {
        Firebase.database.reference.child("envios").child(orderCode).get()
            .addOnSuccessListener { snap ->
                val envio = snap.getValue(EnvioData::class.java) ?: return@addOnSuccessListener
                val sender = envio.sender
                val recipient = envio.recipient
                val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

                val receipt = """
                    === COMPROBANTE SHALOM ENVIOS ===
                    N° de Orden: ${orderCode.substring(0,8)}
                    Código: ${orderCode.substring(8,11)}
                    Fecha: $fecha
                    Estado: Registrado
                    Límite: 24 hrs en agencia

                    ORIGEN
                    Agencia: $origen

                    REMITENTE
                    ${sender?.name} ${sender?.paterno} ${sender?.materno}
                    DNI: ${sender?.dni}
                    Tel: ${sender?.phone}

                    DESTINO
                    Dirección: ${envio.destino}

                    DESTINATARIO
                    ${recipient?.name} ${recipient?.paterno} ${recipient?.materno}
                    DNI: ${recipient?.dni}
                    Tel: ${recipient?.phone}

                    DETALLE
                    Paquete: 1 ${envio.productType.replace("_", " ")}
                    Tarifa: S/ ${envio.price}.00
                    Clave de seguridad: ${envio.pin}

                    =================================
                """.trimIndent()

                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "shalom_envio_$orderCode.txt"
                )
                file.writeText(receipt)
                Toast.makeText(requireContext(), "Descargado en Descargas", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}