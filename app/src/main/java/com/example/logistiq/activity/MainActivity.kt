package com.example.logistiq.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.logistiq.R
import com.example.logistiq.operaciones.BienvenidaFragment
import com.example.logistiq.operaciones.OperacionesActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var bottomNav: BottomNavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_navigation)

        // CONFIGURAR NAVIGATION COMPONENT
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // DATOS DE LOGIN
        val correo = intent.getStringExtra("Correo")
        val proveedor = intent.getStringExtra("Proveedor")

        // FRAGMENTO INICIAL: Bienvenida (solo la primera vez)
        if (savedInstanceState == null) {
            val bundle = Bundle().apply {
                putString("Correo", correo)
                putString("Proveedor", proveedor)
            }
            // USAR ACCIÓN CORRECTA
            navController.navigate(R.id.action_global_bienvenidaFragment, bundle)
        }

        // ESCUCHAR CLICS EN BOTTOM NAV
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    navController.navigate(R.id.action_global_tipoProductoFragment)
                    true
                }
                R.id.nav_perfil -> {
                    val bundle = Bundle().apply {
                        putString("Correo", correo)
                        putString("Proveedor", proveedor)
                    }
                    navController.navigate(R.id.action_global_bienvenidaFragment, bundle)
                    true
                }
                R.id.nav_operaci -> {
                    startActivity(Intent(this, OperacionesActivity::class.java))
                    true
                }
                R.id.nav_config -> {
                    startActivity(Intent(this, MapsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}