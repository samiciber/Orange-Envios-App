package com.example.logistiq.gestion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.logistiq.R
import com.example.logistiq.models.PersonData
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

abstract class BaseFormFragment : Fragment(R.layout.fragment_remitente) {
    protected val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val TAG = "BaseFormFragment"

    // Views
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
        initViews()
        setupSwitches()
        setupClearIcons()
        setupButton()
    }

    private fun initViews() {
        switchDni = requireView().findViewById(R.id.switch_dni)
        switchRuc = requireView().findViewById(R.id.switch_ruc)
        switchCe = requireView().findViewById(R.id.switch_ce)
        etDni = requireView().findViewById(R.id.et_dni)
        etName = requireView().findViewById(R.id.et_name)
        etPaterno = requireView().findViewById(R.id.et_paterno)
        etMaterno = requireView().findViewById(R.id.et_materno)
        etPhone = requireView().findViewById(R.id.et_phone)
        btnRegister = requireView().findViewById(R.id.btn_register)
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
        // Material TextInputLayout maneja el ícono de borrar
    }

    private fun setupButton() {
        btnRegister.setOnClickListener {
            if (validateForm()) {
                saveToRealtimeDB()
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
            else -> "DNI"
        }
    }

    open fun saveToRealtimeDB() {
        val documentType = getDocumentType()
        val documentValue = etDni.text.toString().trim()

        val personData = PersonData(
            dni = documentValue,
            name = etName.text.toString().trim(),
            paterno = etPaterno.text.toString().trim(),
            materno = etMaterno.text.toString().trim(),
            phone = etPhone.text.toString().trim(),
            type = documentType,
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

    open fun clearForm() {
        etDni.text?.clear()
        etName.text?.clear()
        etPaterno.text?.clear()
        etMaterno.text?.clear()
        etPhone.text?.clear()
        switchDni.isChecked = true
        switchRuc.isChecked = false
        switchCe.isChecked = false
    }
}