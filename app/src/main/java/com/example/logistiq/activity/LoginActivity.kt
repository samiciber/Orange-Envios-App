package com.example.logistiq.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.logistiq.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    object Global{
        var preferencias_compartidas="sharedpreferences"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        verificar_sesion_abierta()

        var correo=findViewById<EditText>(R.id.edittext_correo)
        var pass=findViewById<EditText>(R.id.edittext_pass)
        var boton_login=findViewById<Button>(R.id.boton_logear)
        var boton_crear_cuenta=findViewById<Button>(R.id.boton_crear_cuenta)

        boton_login.setOnClickListener{
            if(pass.text.toString()!=""){
                if(correo.text.toString()!="" && Patterns.EMAIL_ADDRESS.matcher(correo.text.toString()).matches()){
                    login_firebase(correo.text.toString(),pass.text.toString())
                }
                else{
                    Toast.makeText(applicationContext,"Formato de correo incorrecto.", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(applicationContext,"Escriba la contraseña", Toast.LENGTH_LONG).show()
            }
        }

        boton_crear_cuenta.setOnClickListener{
            RegistrarActivity().show(supportFragmentManager, null)
        }

    }

    fun login_firebase(correo:String, pass:String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(correo, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    var intent = Intent(applicationContext, MainActivity::class.java)
                    intent.putExtra("Correo",task.result.user?.email)
                    intent.putExtra("Proveedor","Usuario/contraseña")
                    startActivity(intent)

                    guardar_sesion(task.result.user?.email.toString(),"Usuario/contraseña")
                } else {
                    Toast.makeText(applicationContext,"Usuario/contraseña incorrecto(s)", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun verificar_sesion_abierta(){
        var sesion_abierta: SharedPreferences =this.getSharedPreferences(Global.preferencias_compartidas, MODE_PRIVATE)

        var correo=sesion_abierta.getString("Correo",null)
        var proveedor=sesion_abierta.getString("Proveedor",null)

        if(correo!=null && proveedor!=null){
            var intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("Correo",correo)
            intent.putExtra("Proveedor",proveedor)
            startActivity(intent)
        }
    }

    fun guardar_sesion(correo:String, proveedor:String){
        var guardar_sesion: SharedPreferences.Editor=this.getSharedPreferences(Global.preferencias_compartidas, MODE_PRIVATE).edit()
        guardar_sesion.putString("Correo",correo)
        guardar_sesion.putString("Proveedor",proveedor)
        guardar_sesion.apply()
    }
}