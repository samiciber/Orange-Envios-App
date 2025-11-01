package com.example.logistiq.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.logistiq.R
import com.example.logistiq.fragments.BienvenidaFragment
import com.example.logistiq.gestion.RecipientFragment
import com.example.logistiq.gestion.SenderFragment
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

        // Conectar BottomNavigation con NavController
        bottomNav.setupWithNavController(navController)

        // DATOS DE LOGIN
        val correo = intent.getStringExtra("Correo")
        val proveedor = intent.getStringExtra("Proveedor")

        // FRAGMENTO INICIAL: Bienvenida
        if (savedInstanceState == null) {
            replaceFragment(BienvenidaFragment.newInstance(correo, proveedor))
        }

        // ESCUCHAR CLICS EN BOTTOM NAVIGATION
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    // IR AL FLUJO DE ENVÍO: Remitente → Destinatario
                    navController.navigate(R.id.senderFragment)
                    true
                }
                R.id.nav_perfil -> {
                    replaceFragment(BienvenidaFragment.newInstance(correo, proveedor))
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

    // TU MÉTODO ORIGINAL (para fragments fuera de Navigation)
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    // Soporte para el botón "Atrás"
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}