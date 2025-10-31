package com.example.logistiq

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val locations = listOf(
        Pair("Av. Cruz de Motupe", LatLng(-11.93821, -76.97577)),
        Pair("Av. Central", LatLng(-11.94823, -76.97915)),
        Pair("Bayovar", LatLng(-11.95289, -76.98680)),
        Pair("Av. Santa Rosa", LatLng(-11.96530, -76.99753)),
        Pair("Av. circuncición sjl ", LatLng(-11.98096, -77.00106)),
        Pair("los pinos", LatLng(-11.97273, -76.99914)),
        Pair("Canto Grande", LatLng(-11.97315, -77.00606)),
        Pair("SJL - Las Flores", LatLng(-11.98626982832482, -77.01552552048985)),
        Pair("Av. 13 de enero", LatLng(-11.99751780882343, -77.00531237587693)),
        Pair("av. proceres sjl ", LatLng(-12.012671580548341, -77.0023386315131)),
        Pair("jr. chinchaysuyo", LatLng(-12.023919854946284, -77.00100350337165)),
        Pair("el agustino ", LatLng(-12.040507540183565, -76.9975124334499)),
        Pair("av. malecon checa", LatLng(-12.030696435145446, -77.01115704784542)),
        Pair("av. mexico", LatLng(-12.07327279795669, -77.01756369335706)),
        Pair("jr. luna pizarro", LatLng(-12.066419416401434, -77.02694743491101)),
        Pair("Ate", LatLng(-12.0243, -76.9440)),
        Pair("Barranco", LatLng(-12.1506, -77.0219)),
        Pair("Breña", LatLng(-12.0565, -77.0497)),
        Pair("Carabayllo", LatLng(-11.8870, -77.0387)),
        Pair("Chaclacayo", LatLng(-11.9828, -76.7727)),
        Pair("Chorrillos", LatLng(-12.1710, -77.0164)),
        Pair("Cieneguilla", LatLng(-12.0691, -76.8213)),
        Pair("Comas", LatLng(-11.9473, -77.0565)),
        Pair("El Agustino", LatLng(-12.0539, -77.0033)),
        Pair("Independencia", LatLng(-11.9827, -77.0554)),
        Pair("Jesús María", LatLng(-12.0743, -77.0437)),
        Pair("La Molina", LatLng(-12.0833, -76.9167)),
        Pair("La Victoria", LatLng(-12.0667, -77.0167)),
        Pair("Lima (Cercado de Lima)", LatLng(-12.0464, -77.0428)),
        Pair("Lince", LatLng(-12.0860, -77.0333)),
        Pair("Los Olivos", LatLng(-11.9629, -77.0613)),
        Pair("Lurigancho-Chosica", LatLng(-11.9414, -76.7086)),
        Pair("Lurín", LatLng(-12.2737, -76.8780)),
        Pair("Magdalena del Mar", LatLng(-12.0948, -77.0705)),
        Pair("Miraflores", LatLng(-12.1211, -77.0307)),
        Pair("Pachacámac", LatLng(-12.2269, -76.8336)),
        Pair("Pucusana", LatLng(-12.4765, -76.7968)),
        Pair("Pueblo Libre", LatLng(-12.0731, -77.0719)),
        Pair("Puente Piedra", LatLng(-11.8651, -77.0750)),
        Pair("Punta Hermosa", LatLng(-12.3175, -76.8120)),
        Pair("Punta Negra", LatLng(-12.3619, -76.7874)),
        Pair("Rímac", LatLng(-12.0304, -77.0330)),
        Pair("San Bartolo", LatLng(-12.3928, -76.7744)),
        Pair("San Borja", LatLng(-12.1026, -77.0133)),
        Pair("San Isidro", LatLng(-12.0970, -77.0377)),
        Pair("San Juan de Lurigancho", LatLng(-11.9822, -76.9950)),
        Pair("San Juan de Miraflores", LatLng(-12.1603, -76.9711)),
        Pair("San Luis", LatLng(-12.0782, -77.0048)),
        Pair("San Martín de Porres", LatLng(-11.9840, -77.0712)),
        Pair("San Miguel", LatLng(-12.0784, -77.0922)),
        Pair("Santa Anita", LatLng(-12.0492, -76.9720)),
        Pair("Santa María del Mar", LatLng(-12.3848, -76.7811)),
        Pair("Santa Rosa", LatLng(-11.7724, -77.1591)),
        Pair("Santiago de Surco", LatLng(-12.1421, -76.9994)),
        Pair("Surquillo", LatLng(-12.1209, -77.0219)),
        Pair("Villa El Salvador", LatLng(-12.2267, -76.9506)),
        Pair("Villa María del Triunfo", LatLng(-12.1851, -76.9447)),
        Pair("Amazonas", LatLng(-5.1197, -78.1834)),
        Pair("Áncash", LatLng(-9.5297, -77.5272)),
        Pair("Apurímac", LatLng(-14.0459, -73.0544)),
        Pair("Arequipa", LatLng(-16.3989, -71.5369)),
        Pair("Ayacucho", LatLng(-13.1588, -74.2232)),
        Pair("Cajamarca", LatLng(-7.1617, -78.5128)),
        Pair("Callao", LatLng(-12.0616, -77.1283)),
        Pair("Cusco", LatLng(-13.5319, -71.9675)),
        Pair("Huancavelica", LatLng(-12.7867, -74.9730)),
        Pair("Huánuco", LatLng(-9.9306, -76.2422)),
        Pair("Ica", LatLng(-14.0678, -75.7286)),
        Pair("Junín", LatLng(-11.1582, -75.9931)),
        Pair("La Libertad", LatLng(-8.1119, -79.0288)),
        Pair("Lambayeque", LatLng(-6.7011, -79.9061)),
        Pair("Lima", LatLng(-12.0464, -77.0428)),
        Pair("Loreto", LatLng(-3.7491, -73.2538)),
        Pair("Madre de Dios", LatLng(-12.5926, -69.1894)),
        Pair("Moquegua", LatLng(-17.1954, -70.9357)),
        Pair("San Martín", LatLng(-6.4846, -76.3729)),
        Pair("Tacna", LatLng(-18.0066, -70.2463)),
        Pair("Tumbes", LatLng(-3.5669, -80.4515)),
        Pair("Ucayali", LatLng(-8.3791, -74.5539)),
        Pair("Pasco", LatLng(-10.6848, -76.2560)),
        Pair("Piura", LatLng(-5.1945, -80.6328)),
        Pair("Puno", LatLng(-15.8402, -70.0219)),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        val backButton = findViewById<ImageView>(R.id.btnReturn)
        backButton.setOnClickListener {
            val intent = Intent(this, Bienvenida::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        for (location in locations) {
            mMap.addMarker(MarkerOptions().position(location.second).title(location.first))
        }

        val initialLocation = LatLng(-11.982281903462482, -77.00470798327116)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 13f))

        val agencyNames = locations.map { it.first }
        val adapter = ArrayAdapter(this, R.layout.dropdown_item_grey, agencyNames)
        val searchAutoComplete = findViewById<AutoCompleteTextView>(R.id.search_autocomplete_text_view)
        searchAutoComplete.setAdapter(adapter)

        searchAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val selectedAgencyName = parent.getItemAtPosition(position) as String
            val selectedAgency = locations.find { it.first == selectedAgencyName }
            selectedAgency?.let { 
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it.second, 15f))
            }
        }
    }
}
