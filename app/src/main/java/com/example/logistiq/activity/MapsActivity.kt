package com.example.logistiq.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.logistiq.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

data class LocationData(val name: String = "", val latitude: Double = 0.0, val longitude: Double = 0.0)
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var database: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener
    private val locationsLatLng = mutableListOf<Pair<String, LatLng>>()
    private lateinit var searchAutoComplete: AutoCompleteTextView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        database = Firebase.database.reference.child("locations")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        val backButton = findViewById<ImageView>(R.id.btnReturn)
        backButton.setOnClickListener {
            finish()
        }

        val agenciesButton = findViewById<Button>(R.id.agencies_button)
        agenciesButton.setOnClickListener {
            searchAutoComplete.showDropDown()
        }

        setupAutoComplete()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val initialLocation = LatLng(-11.982281903462482, -77.00470798327116)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 13f))

        fetchLocationsFromFirebase()
    }

    private fun fetchLocationsFromFirebase() {
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isFinishing || isDestroyed) {
                    return
                }
                locationsLatLng.clear()

                val newLocations = mutableListOf<Pair<String, LatLng>>()
                val agencyNames = mutableListOf<String>()
                for (locationSnapshot in snapshot.children) {
                    val location = locationSnapshot.getValue(LocationData::class.java)
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        newLocations.add(Pair(it.name, latLng))
                        agencyNames.add(it.name)
                    }
                }
                locationsLatLng.addAll(newLocations)

                updateMarkers(searchAutoComplete.text.toString())

                adapter.clear()
                adapter.addAll(agencyNames)
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // Log error or show a message to the user
            }
        }
        database.addValueEventListener(valueEventListener)
    }

    private fun setupAutoComplete() {
        searchAutoComplete = findViewById(R.id.search_autocomplete_text_view)
        adapter = ArrayAdapter(this, R.layout.dropdown_item_grey, mutableListOf<String>())
        searchAutoComplete.setAdapter(adapter)

        searchAutoComplete.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // El componente AutoCompleteTextView se encarga de filtrar las sugerencias
                // del dropdown automáticamente. Solo necesitamos esta llamada para actualizar
                // los marcadores en el mapa en tiempo real.
                updateMarkers(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Al seleccionar una sugerencia
        searchAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val selectedAgencyName = parent.getItemAtPosition(position) as String
            val selectedAgency = locationsLatLng.find { it.first == selectedAgencyName }
            selectedAgency?.let {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it.second, 15f))
            }
        }
    }

    private fun updateMarkers(searchText: String) {
        if (!::mMap.isInitialized) return

        mMap.clear()
        val filteredLocations = if (searchText.isBlank()) {
            locationsLatLng
        } else {
            locationsLatLng.filter {
                it.first.contains(searchText, ignoreCase = true)
            }
        }

        filteredLocations.forEach { (name, latLng) ->
            mMap.addMarker(MarkerOptions().position(latLng).title(name))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::valueEventListener.isInitialized) {
            database.removeEventListener(valueEventListener)
        }
    }
}