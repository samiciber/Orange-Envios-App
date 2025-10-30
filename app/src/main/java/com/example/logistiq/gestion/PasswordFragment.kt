package com.example.logistiq.gestion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.logistiq.R
import android.widget.EditText
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import android.widget.ImageButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var passwordInput: EditText
    private lateinit var btnCreateOrder: Button
    private lateinit var keyboardGrid: GridLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passwordInput = view.findViewById(R.id.et_password_input)
        btnCreateOrder = view.findViewById(R.id.btn_create_order)
        keyboardGrid = view.findViewById(R.id.keyboard_grid)

        btnCreateOrder.isEnabled = false

        setupKeyboardListeners()

        btnCreateOrder.setOnClickListener {
            val password = passwordInput.text.toString()
            if (password.length == 4) {
                Toast.makeText(context, "Clave '$password' registrada. Creando orden...", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "La clave debe ser de 4 dígitos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupKeyboardListeners() {
        val clickListener = View.OnClickListener { v ->
            val currentText = passwordInput.text.toString()
            if (currentText.length < 4 && v is Button) {
                passwordInput.append(v.text)
                checkPasswordLength()
            }
        }

        for (i in 0 until keyboardGrid.childCount) {
            val child = keyboardGrid.getChildAt(i)
            if (child is Button) {
                child.setOnClickListener(clickListener)
            }
        }

        view?.findViewById<ImageButton>(R.id.btn_clear_all)?.setOnClickListener {
            passwordInput.setText("")
            checkPasswordLength()
        }

        view?.findViewById<ImageButton>(R.id.btn_backspace)?.setOnClickListener {
            val currentText = passwordInput.text
            if (currentText.isNotEmpty()) {
                passwordInput.setText(currentText.substring(0, currentText.length - 1))
                checkPasswordLength()
            }
        }
    }

    private fun checkPasswordLength() {
        btnCreateOrder.isEnabled = passwordInput.text.length == 4
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}