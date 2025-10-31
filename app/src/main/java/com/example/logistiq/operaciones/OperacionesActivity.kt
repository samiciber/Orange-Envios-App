package com.example.logistiq.operaciones

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.logistiq.Bienvenida
import com.example.logistiq.R

class OperacionesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operaciones)

        val backButton = findViewById<ImageView>(R.id.btnReturn)
        backButton.setOnClickListener {
            val intent = Intent(this, Bienvenida::class.java)
            startActivity(intent)
            finish()
        }
    }
}
