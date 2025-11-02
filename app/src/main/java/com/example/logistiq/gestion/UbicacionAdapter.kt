package com.example.logistiq.gestion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.logistiq.models.Ubicacion

class UbicacionAdapter(
    private val ubicaciones: List<Ubicacion>,
    private val onItemClick: (Ubicacion) -> Unit
) : RecyclerView.Adapter<UbicacionAdapter.ViewHolder>() {

    private val filteredList = mutableListOf<Ubicacion>()

    init {
        filteredList.addAll(ubicaciones)
    }

    fun filter(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(ubicaciones)
        } else {
            filteredList.addAll(ubicaciones.filter {
                it.name.contains(query, ignoreCase = true)
            })
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ubicacion = filteredList[position]
        holder.textView.text = ubicacion.name
        holder.itemView.setOnClickListener { onItemClick(ubicacion) }
    }

    override fun getItemCount() = filteredList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
    }
}