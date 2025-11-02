package com.example.logistiq.gestion

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.logistiq.R
import androidx.navigation.fragment.findNavController
import com.example.logistiq.databinding.FragmentDetailBinding
import com.example.logistiq.models.EnvioData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.File
import kotlin.jvm.java

class DetailFragment: Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderCode = DetailFragmentArgs.fromBundle(requireArguments()).orderCode

        Firebase.database.reference.child("envios").child(orderCode).get()
            .addOnSuccessListener { snap ->
                val envio = snap.getValue(EnvioData::class.java) ?: return@addOnSuccessListener

                binding.tvDate.text = envio.fecha
                binding.tvOrigen.text = envio.origen
                binding.tvDestino.text = envio.destino

                binding.tvRemitente.text = "${envio.sender?.name} ${envio.sender?.paterno} ${envio.sender?.materno}"
                binding.tvDniRemitente.text = "DNI : ${envio.sender?.dni}"

                binding.tvDestinatario.text = "${envio.recipient?.name} ${envio.recipient?.paterno} ${envio.recipient?.materno}"
                binding.tvDniDestinatario.text = "DNI : ${envio.recipient?.dni}"

                binding.tvPaquete.text = "1 ${envio.productType.replace("_", " ").capitalize()}"
                binding.tvTarifa.text = "Tarifa : S/. ${envio.price}"
                binding.tvPin.text = envio.pin
            }

        binding.btnDescargar.setOnClickListener { downloadReceipt(orderCode) }
        binding.btnOtroEnvio.setOnClickListener {
            findNavController().navigate(R.id.action_detail_to_tipoProducto)
        }
    }

    private fun downloadReceipt(orderCode: String) {
        Firebase.database.reference.child("envios").child(orderCode).get()
            .addOnSuccessListener { snap ->
                val envio = snap.getValue(EnvioData::class.java) ?: return@addOnSuccessListener
                val receipt = """
                    === DETALLE DE ENVÍO SHALOM ===
                    Orden: ${orderCode.substring(0,8)}
                    Código: ${orderCode.substring(8,11)}
                    Fecha: ${envio.fecha}
                    
                    ORIGEN: ${envio.origen}
                    DESTINO: ${envio.destino}
                    
                    REMITENTE: ${envio.sender?.name} ${envio.sender?.paterno}
                    DNI: ${envio.sender?.dni}
                    
                    DESTINATARIO: ${envio.recipient?.name} ${envio.recipient?.paterno}
                    DNI: ${envio.recipient?.dni}
                    
                    PAQUETE: 1 ${envio.productType.replace("_", " ")}
                    TARIFA: S/ ${envio.price}.00
                    CLAVE: ${envio.pin}
                """.trimIndent()

                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "shalom_detalle_$orderCode.txt"
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