package com.example.logistiq.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.logistiq.MainActivity
import com.example.logistiq.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class BienvenidaFragment : Fragment() {

    private var correo: String? = null
    private var proveedor: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            correo = it.getString(ARG_CORREO)
            proveedor = it.getString(ARG_PROVEEDOR)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bienvenida, container, false)

        val correoUsuario = view.findViewById<TextView>(R.id.correo_usuario)
        val proveedorServicio = view.findViewById<TextView>(R.id.proveedor_servicio)
        val botonSalir = view.findViewById<Button>(R.id.boton_salir)

        correoUsuario.text = "Correo: $correo"
        proveedorServicio.text = "Proveedor: $proveedor"

        botonSalir.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            borrarSesion()
        }

        return view
    }

    private fun borrarSesion() {
        val prefs = requireContext().getSharedPreferences(
            MainActivity.Global.preferencias_compartidas,
            Context.MODE_PRIVATE
        ).edit()
        prefs.clear()
        prefs.apply()
        prefs.commit()
        Firebase.auth.signOut()
    }

    companion object {
        private const val ARG_CORREO = "Correo"
        private const val ARG_PROVEEDOR = "Proveedor"

        fun newInstance(correo: String?, proveedor: String?) =
            BienvenidaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CORREO, correo)
                    putString(ARG_PROVEEDOR, proveedor)
                }
            }
    }
}
