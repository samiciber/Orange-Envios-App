package com.example.logistiq.gestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.logistiq.databinding.FragmentResumenTarifaBinding
import kotlin.math.*
class FragmentResumenTarifa : Fragment() {
    private var _binding: FragmentResumenTarifaBinding? = null
    private val binding get() = _binding!!

    private var cantidad = 1
    private val precioPorKm = 0.6

    private var origenLat = 0.0
    private var origenLng = 0.0
    private var destinoLat = 0.0
    private var destinoLng = 0.0
    private var productType: String = "sobre"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResumenTarifaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = FragmentResumenTarifaArgs.fromBundle(requireArguments())
        productType = args.productType

        binding.tvOrigenNombre.text = args.origenNombre
        binding.tvDestinoNombre.text = args.destinoNombre
        binding.tvDestinoDireccion.text = args.destino

        val origenCoords = args.origen.split(",")
        val destinoCoords = args.destino.split(",")
        origenLat = origenCoords[0].toDouble()
        origenLng = origenCoords[1].toDouble()
        destinoLat = destinoCoords[0].toDouble()
        destinoLng = destinoCoords[1].toDouble()

        binding.tvTipoPaquete.text = when (productType) {
            "sobre" -> "Sobre (Documentos simples en sobre manila / Tamaño A4)"
            "caja_pequena" -> "Caja pequeña (hasta 30x20x10 cm)"
            "caja_mediana" -> "Caja mediana (hasta 50x30x20 cm)"
            "caja_grande" -> "Caja grande (hasta 70x50x40 cm)"
            "otra_medida" -> "Otra medida"
            else -> "Paquete"
        }

        updatePrecio()

        binding.btnMas.setOnClickListener { cantidad++; updatePrecio() }
        binding.btnMenos.setOnClickListener { if (cantidad > 1) cantidad-- ; updatePrecio() }

        binding.btnContinuar.setOnClickListener {
            val distancia = calcularDistancia(origenLat, origenLng, destinoLat, destinoLng)
            val precioBase = getPrecioBase(productType)
            val total = (precioBase + (distancia * precioPorKm)) * cantidad

            val action = FragmentResumenTarifaDirections.actionResumenTarifaToSender(
                productType = productType,
                price = total.toInt(),
                origenNombre = args.origenNombre,
                destinoNombre = args.destinoNombre
            )
            findNavController().navigate(action)
        }
    }

    private fun updatePrecio() {
        binding.tvCantidad.text = cantidad.toString()
        val distancia = calcularDistancia(origenLat, origenLng, destinoLat, destinoLng)
        val precioBase = getPrecioBase(productType)
        val total = (precioBase + (distancia * precioPorKm)) * cantidad

        binding.tvDistancia.text = "${String.format("%.2f", distancia)} km"
        binding.tvPrecio.text = "S/ ${String.format("%.2f", total)}"
    }

    private fun getPrecioBase(tipo: String): Double = when (tipo) {
        "sobre" -> 8.0
        "caja_pequena" -> 15.0
        "caja_mediana" -> 30.0
        "caja_grande" -> 40.0
        "otra_medida" -> 60.0
        else -> 8.0
    }

    private fun calcularDistancia(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}