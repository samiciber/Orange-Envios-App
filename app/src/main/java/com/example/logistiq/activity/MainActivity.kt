package com.example.logistiq.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.logistiq.R
import com.example.logistiq.fragments.BienvenidaFragment
import com.example.logistiq.gestion.RecipientFragment
import com.example.logistiq.operaciones.OperacionesActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Fragmento inicial
        val correo = intent.getStringExtra("Correo")
        val proveedor = intent.getStringExtra("Proveedor")
        replaceFragment(BienvenidaFragment.Companion.newInstance(correo, proveedor))

        // Escuchar la navegación
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> replaceFragment(RecipientFragment())
                R.id.nav_perfil -> replaceFragment(BienvenidaFragment.Companion.newInstance(correo, proveedor))
                R.id.nav_operaci -> {
                    val intent = Intent(this, OperacionesActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_config -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}