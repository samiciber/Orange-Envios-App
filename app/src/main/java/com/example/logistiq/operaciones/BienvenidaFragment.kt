package com.example.logistiq.operaciones

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.logistiq.activity.LoginActivity
import com.example.logistiq.databinding.FragmentBienvenidaBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class BienvenidaFragment : Fragment() {

    private var _binding: FragmentBienvenidaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBienvenidaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val correo = arguments?.getString("Correo") ?: "No disponible"
        val proveedor = arguments?.getString("Proveedor") ?: "No disponible"

        binding.correoUsuario.text = "Correo: $correo"
        binding.proveedorServicio.text = "Proveedor: $proveedor"

        binding.botonSalir.setOnClickListener {
            borrarSesion()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun borrarSesion() {
        val prefs = requireContext().getSharedPreferences(
            LoginActivity.Global.preferencias_compartidas,
            Context.MODE_PRIVATE
        ).edit()
        prefs.clear().apply()
        Firebase.auth.signOut()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}