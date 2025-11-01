package com.example.logistiq.gestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.logistiq.R
import com.google.android.material.button.MaterialButton
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation.findNavController

class TipoProductoFragment: Fragment() {
    private var selectedType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tipo_producto, container, false)

        // === TARJETAS ===
        val cardSobre = view.findViewById<CardView>(R.id.card_sobre)
        val cardCajaPequena = view.findViewById<CardView>(R.id.card_caja_pequena)
        val cardCajaMediana = view.findViewById<CardView>(R.id.card_caja_mediana)
        val cardCajaGrande = view.findViewById<CardView>(R.id.card_caja_grande)
        val cardOtraMedida = view.findViewById<CardView>(R.id.card_otra_medida)

        val btnContinuar = view.findViewById<MaterialButton>(R.id.btn_continuar)

        // === FUNCIÓN DE SELECCIÓN ===
        val selectCard = { selectedCard: CardView, type: String ->
            // Resetear opacidad
            listOf(cardSobre, cardCajaPequena, cardCajaMediana, cardCajaGrande, cardOtraMedida).forEach {
                it.alpha = 0.6f
            }
            // Resaltar seleccionada
            selectedCard.alpha = 1f
            selectedType = type
            btnContinuar.isEnabled = true
            btnContinuar.alpha = 1f
        }

        // === CLICS ===
        cardSobre.setOnClickListener { selectCard(cardSobre, "sobre") }
        cardCajaPequena.setOnClickListener { selectCard(cardCajaPequena, "caja_pequena") }
        cardCajaMediana.setOnClickListener { selectCard(cardCajaMediana, "caja_mediana") }
        cardCajaGrande.setOnClickListener { selectCard(cardCajaGrande, "caja_grande") }
        cardOtraMedida.setOnClickListener { selectCard(cardOtraMedida, "otra_medida") }

        // === BOTÓN CONTINUAR ===
        btnContinuar.setOnClickListener {
            selectedType?.let { type ->
                val bundle = Bundle().apply {
                    putString("productType", type)
                }
                findNavController().navigate(
                    R.id.action_tipoProducto_to_sender,
                    bundle
                )
            }
        }

        // === TOOLBAR (flecha atrás) ===
        view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
            .setNavigationOnClickListener {
                findNavController().navigateUp()
            }

        return view
    }
}