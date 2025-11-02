package com.example.logistiq.gestion

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.logistiq.R
import com.example.logistiq.databinding.FragmentSeleccionUbicacionBinding
import com.example.logistiq.models.Ubicacion
import com.google.firebase.database.*

class FragmentSeleccionUbicacion : Fragment() {
    private var _binding: FragmentSeleccionUbicacionBinding? = null
    private val binding get() = _binding!!

    private val database = FirebaseDatabase.getInstance().reference.child("locations")
    private val ubicacionesList = mutableListOf<Ubicacion>()
    private lateinit var adapterOrigen: ArrayAdapter<String>
    private lateinit var adapterDestino: ArrayAdapter<String>
    private lateinit var sugerenciasAdapter: SugerenciasAdapter

    private var origenSeleccionado: Ubicacion? = null
    private var destinoSeleccionado: Ubicacion? = null
    private var productType: String = "sobre"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeleccionUbicacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productType = FragmentSeleccionUbicacionArgs.fromBundle(requireArguments()).productType

        setupAdapters()
        setupRecyclerView()
        loadUbicacionesFromFirebase()

        // Toolbar
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Forzar dropdown
        binding.actvOrigen.setOnClickListener { binding.actvOrigen.showDropDown() }
        binding.tilOrigen.setOnClickListener { binding.actvOrigen.showDropDown() }
        binding.actvDestino.setOnClickListener { binding.actvDestino.showDropDown() }
        binding.tilDestino.setOnClickListener { binding.actvDestino.showDropDown() }

        // FILTRO EN TIEMPO REAL
        binding.actvOrigen.addTextChangedListener(createFilter(adapterOrigen))
        binding.actvDestino.addTextChangedListener(createFilter(adapterDestino))

        // SELECCIÓN
        binding.actvOrigen.setOnItemClickListener { _, _, position, _ ->
            val nombre = adapterOrigen.getItem(position) ?: return@setOnItemClickListener
            origenSeleccionado = ubicacionesList.find { it.name == nombre }
            updateUI()
        }

        binding.actvDestino.setOnItemClickListener { _, _, position, _ ->
            val nombre = adapterDestino.getItem(position) ?: return@setOnItemClickListener
            destinoSeleccionado = ubicacionesList.find { it.name == nombre }
            if (destinoSeleccionado?.id == origenSeleccionado?.id) {
                binding.tilDestino.error = "Destino debe ser diferente"
                destinoSeleccionado = null
            } else {
                binding.tilDestino.error = null
                navigateToResumen()
            }
        }
    }

    private fun createFilter(adapter: ArrayAdapter<String>): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun setupAdapters() {
        adapterOrigen = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)
        adapterDestino = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)
        binding.actvOrigen.setAdapter(adapterOrigen)
        binding.actvDestino.setAdapter(adapterDestino)
    }

    private fun setupRecyclerView() {
        sugerenciasAdapter = SugerenciasAdapter { ubicacion ->
            if (origenSeleccionado == null) {
                origenSeleccionado = ubicacion
            } else if (destinoSeleccionado == null && ubicacion.id != origenSeleccionado?.id) {
                destinoSeleccionado = ubicacion
                navigateToResumen()
            }
            updateUI()
        }
        binding.rvSugerencias.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = sugerenciasAdapter
        }
    }

    private fun loadUbicacionesFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ubicacionesList.clear()
                for (child in snapshot.children) {
                    val id = child.key ?: continue
                    val name = child.child("name").getValue(String::class.java) ?: ""
                    val lat = child.child("latitude").getValue(Double::class.java) ?: 0.0
                    val lng = child.child("longitude").getValue(Double::class.java) ?: 0.0
                    if (name.isNotEmpty()) {
                        ubicacionesList.add(Ubicacion(id, name, lat, lng))
                    }
                }
                updateAdapters()
                updateUI()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateAdapters() {
        val nombres = ubicacionesList.map { it.name }
        adapterOrigen.clear()
        adapterOrigen.addAll(nombres)
        adapterDestino.clear()
        adapterDestino.addAll(nombres.filter { it != origenSeleccionado?.name })
    }

    private fun updateUI() {
        binding.actvOrigen.setText(origenSeleccionado?.name ?: "", false)
        binding.actvDestino.setText(destinoSeleccionado?.name ?: "", false)

        if (origenSeleccionado != null && destinoSeleccionado == null) {
            binding.rvSugerencias.visibility = View.VISIBLE
            val sugerencias = ubicacionesList.filter { it.id != origenSeleccionado?.id }
            sugerenciasAdapter.submitList(sugerencias)
        } else {
            binding.rvSugerencias.visibility = View.GONE
        }

        updateAdapters()
    }

    private fun navigateToResumen() {
        if (origenSeleccionado != null && destinoSeleccionado != null) {
            val action = FragmentSeleccionUbicacionDirections.actionSeleccionUbicacionToResumenTarifa(
                origen = "${origenSeleccionado!!.latitude},${origenSeleccionado!!.longitude}",
                destino = "${destinoSeleccionado!!.latitude},${destinoSeleccionado!!.longitude}",
                destinoNombre = destinoSeleccionado!!.name,
                productType = productType,
                origenNombre = origenSeleccionado!!.name
            )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}