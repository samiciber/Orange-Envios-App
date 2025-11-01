package com.example.logistiq.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.logistiq.activity.LoginActivity
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

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_bienvenida, container, false)

        val correoUsuario = view.findViewById<TextView>(R.id.correo_usuario)
        val proveedorServicio = view.findViewById<TextView>(R.id.proveedor_servicio)
        val botonSalir = view.findViewById<Button>(R.id.boton_salir)

        correoUsuario.text = "Correo: $correo"
        proveedorServicio.text = "Proveedor: $proveedor"

        botonSalir.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            borrarSesion()
        }

        return view
    }

    private fun borrarSesion() {
        val prefs = requireContext().getSharedPreferences(
            LoginActivity.Global.preferencias_compartidas,
            Context.MODE_PRIVATE
        ).edit()
        prefs.clear()
        prefs.apply()
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
