package com.example.logistiq.gestion

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.logistiq.R
import com.example.logistiq.models.PersonData
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference

abstract class BaseFormFragment : Fragment() {
    protected val database: DatabaseReference = FirebaseDatabase.getInstance().reference  // ← Realtime DB Reference
    private val TAG = "BaseFormFragment"

    // Views (IDs unificados)
    protected lateinit var switchDni: MaterialSwitch
    protected lateinit var switchRuc: MaterialSwitch
    protected lateinit var switchCe: MaterialSwitch
    protected lateinit var etDni: TextInputEditText
    protected lateinit var etName: TextInputEditText
    protected lateinit var etPaterno: TextInputEditText
    protected lateinit var etMaterno: TextInputEditText
    protected lateinit var etPhone: TextInputEditText
    protected lateinit var btnRegister: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupSwitches()
        setupClearIcons()
        setupButton()
    }

    private fun initViews(view: View) {
        switchDni = view.findViewById(R.id.switch_dni)
        switchRuc = view.findViewById(R.id.switch_ruc)
        switchCe = view.findViewById(R.id.switch_ce)
        etDni = view.findViewById(R.id.et_dni)
        etName = view.findViewById(R.id.et_name)
        etPaterno = view.findViewById(R.id.et_paterno)
        etMaterno = view.findViewById(R.id.et_materno)
        etPhone = view.findViewById(R.id.et_phone)
        btnRegister = view.findViewById(R.id.btn_register)
    }

    private fun setupSwitches() {
        switchDni.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchRuc.isChecked = false
                switchCe.isChecked = false
                etDni.hint = "DNI"
                etDni.inputType = android.text.InputType.TYPE_CLASS_NUMBER
            }
        }
        switchRuc.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchDni.isChecked = false
                switchCe.isChecked = false
                etDni.hint = "RUC"
                etDni.inputType = android.text.InputType.TYPE_CLASS_NUMBER
            }
        }
        switchCe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchDni.isChecked = false
                switchRuc.isChecked = false
                etDni.hint = "CE"
                etDni.inputType = android.text.InputType.TYPE_CLASS_TEXT
            }
        }
    }

    private fun setupClearIcons() {
        // Material maneja clear_text automáticamente
    }

    private fun setupButton() {
        btnRegister.setOnClickListener {
            if (validateForm()) {
                saveToRealtimeDB()  // ← FUNCIÓN CORREGIDA PARA REALTIME DB
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateForm(): Boolean {
        val phoneStr = etPhone.text.toString().trim()
        return etDni.text.toString().trim().isNotEmpty() &&
                etName.text.toString().trim().isNotEmpty() &&
                etPaterno.text.toString().trim().isNotEmpty() &&
                etMaterno.text.toString().trim().isNotEmpty() &&
                phoneStr.isNotEmpty() && phoneStr.length >= 9
    }

    protected open fun getDocumentType(): String {
        return when {
            switchDni.isChecked -> "DNI"
            switchRuc.isChecked -> "RUC"
            switchCe.isChecked -> "CE"
            else -> "DNI" // fallback
        }
    }

    // ← FUNCIÓN CORREGIDA: GUARDA EN REALTIME DATABASE
    open fun saveToRealtimeDB() {
        // DETERMINAR TIPO CORRECTAMENTE (usando switches, no hint)
        val documentType = getDocumentType()

        // TOMAR EL VALOR DEL CAMPO DNI/RUC/CE (el mismo campo)
        val documentValue = etDni.text.toString().trim()

        val personData = PersonData(
            dni = documentValue,           // Aquí va el número (DNI, RUC, CE)
            name = etName.text.toString().trim(),
            paterno = etPaterno.text.toString().trim(),
            materno = etMaterno.text.toString().trim(),
            phone = etPhone.text.toString().trim(),
            type = documentType,           // Aquí va el TIPO: "DNI", "RUC", "CE"
            isSender = this is SenderFragment
        )

        val ref = if (personData.isSender) {
            database.child("senders").push()
        } else {
            database.child("recipients").push()
        }

        ref.setValue(personData.toMap())
            .addOnSuccessListener {
                Log.d(TAG, "Datos guardados correctamente")
                Toast.makeText(requireContext(), "¡Registrado exitosamente!", Toast.LENGTH_SHORT).show()
                clearForm()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al guardar", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    protected fun clearForm() {
        etDni.text?.clear()
        etName.text?.clear()
        etPaterno.text?.clear()
        etMaterno.text?.clear()
        etPhone.text?.clear()
        switchDni.isChecked = true  // Reset a DNI
        switchRuc.isChecked = false
        switchCe.isChecked = false
    }
}