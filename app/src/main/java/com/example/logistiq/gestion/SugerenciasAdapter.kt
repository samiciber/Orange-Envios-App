package com.example.logistiq.gestion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.logistiq.databinding.ItemUbicacionBinding
import com.example.logistiq.models.Ubicacion

class SugerenciasAdapter (
    private val onClick: (Ubicacion) -> Unit
) : ListAdapter<Ubicacion, SugerenciasAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUbicacionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemUbicacionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ubicacion: Ubicacion) {
            binding.tvNombre.text = ubicacion.name
            binding.tvDetalle.text = "PERÚ / ${ubicacion.name.uppercase()}"
            binding.root.setOnClickListener {
                onClick(ubicacion)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Ubicacion>() {
        override fun areItemsTheSame(old: Ubicacion, new: Ubicacion) = old.id == new.id
        override fun areContentsTheSame(old: Ubicacion, new: Ubicacion) = old == new
    }
}