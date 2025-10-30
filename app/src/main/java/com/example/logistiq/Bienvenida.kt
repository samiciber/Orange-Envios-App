package com.example.logistiq

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.logistiq.fragments.BienvenidaFragment
import com.example.logistiq.gestion.RecipientFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
class Bienvenida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Fragmento inicial
        val correo = intent.getStringExtra("Correo")
        val proveedor = intent.getStringExtra("Proveedor")
        replaceFragment(BienvenidaFragment.newInstance(correo, proveedor))

        // Escuchar la navegación
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> replaceFragment(RecipientFragment())
                R.id.nav_perfil -> replaceFragment(BienvenidaFragment.newInstance(correo, proveedor))
                R.id.nav_config -> replaceFragment(ConfigFragment())
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