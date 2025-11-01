package com.example.logistiq.gestion

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.logistiq.R
import com.example.logistiq.models.PersonData  // ← IMPORTA ESTE
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore  // ← IMPORTA ESTE

abstract class BaseFormFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()  // ← INSTANCIA DE FIRESTORE
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
        // Material ya maneja el clear_text
    }

    private fun setupButton() {
        btnRegister.setOnClickListener {
            if (validateForm()) {
                saveToFirebase()  // ← NUEVA FUNCIÓN: ENVÍA A FIREBASE
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateForm(): Boolean {
        return etDni.text?.isNotBlank() == true &&
                etName.text?.isNotBlank() == true &&
                etPaterno.text?.isNotBlank() == true &&
                etMaterno.text?.isNotBlank() == true &&
                etPhone.text?.toString()?.length!! >= 9
    }

    // ← NUEVA FUNCIÓN: GUARDA EN FIREBASE
    private fun saveToFirebase() {
        val documentType = when {
            switchDni.isChecked -> "DNI"
            switchRuc.isChecked -> "RUC"
            else -> "CE"
        }

        val personData = PersonData(
            dni = etDni.text.toString().trim(),
            name = etName.text.toString().trim(),
            paterno = etPaterno.text.toString().trim(),
            materno = etMaterno.text.toString().trim(),
            phone = etPhone.text.toString().trim(),
            type = documentType,
            isSender = this is SenderFragment  // Distingue remitente/destinatario
        )

        // Colección "personas" en Firestore
        val collectionRef = if (personData.isSender) {
            db.collection("senders")  // Para remitentes
        } else {
            db.collection("recipients")  // Para destinatarios
        }

        collectionRef.add(personData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Documento agregado con ID: ${documentReference.id}")
                Toast.makeText(requireContext(), "¡Datos enviados a Firebase exitosamente!", Toast.LENGTH_SHORT).show()
                clearForm()
                // Opcional: Navega a siguiente pantalla
                // findNavController().navigate(R.id.action_to_next_fragment)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error al agregar documento", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearForm() {
        etDni.text?.clear()
        etName.text?.clear()
        etPaterno.text?.clear()
        etMaterno.text?.clear()
        etPhone.text?.clear()
        switchDni.isChecked = true  // Reset a DNI
    }
}