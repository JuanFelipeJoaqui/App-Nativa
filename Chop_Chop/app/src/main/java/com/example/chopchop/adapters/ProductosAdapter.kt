package com.example.chopchop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chopchop.R
import com.example.chopchop.models.Producto

class ProductosAdapter(
    private val onAgregarClick: (Producto) -> Unit,
    private val onQuitarClick: (Producto) -> Unit,
    private val onEliminarClick: (Producto) -> Unit
) : ListAdapter<Producto, ProductosAdapter.ProductoViewHolder>(ProductoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = getItem(position)
        holder.bind(producto)
    }

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)
        private val tvNombreProducto: TextView = itemView.findViewById(R.id.tvNombreProducto)
        private val btnAgregar: ImageButton = itemView.findViewById(R.id.btnAgregar)
        private val btnQuitar: ImageButton = itemView.findViewById(R.id.btnQuitar)
        private val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)

        fun bind(producto: Producto) {
            tvCantidad.text = producto.cantidad.toString()
            tvNombreProducto.text = producto.nombre

            if (producto.completado) {
                tvNombreProducto.alpha = 0.5f
                tvCantidad.alpha = 0.5f
            } else {
                tvNombreProducto.alpha = 1f
                tvCantidad.alpha = 1f
            }

            btnAgregar.setOnClickListener { onAgregarClick(producto) }
            btnQuitar.setOnClickListener { onQuitarClick(producto) }
            btnEliminar.setOnClickListener { onEliminarClick(producto) }

            // Cambiar el color del círculo según la categoría
            val colorRes = when (producto.categoria) {
                Categoria.FRUTAS_VERDURAS -> R.color.green
                Categoria.CARNES -> R.color.red
                Categoria.LACTEOS -> R.color.blue
                Categoria.PANADERIA -> R.color.brown
                Categoria.BEBIDAS -> R.color.purple
                Categoria.LIMPIEZA -> R.color.cyan
                Categoria.DESPENSA -> R.color.orange
                Categoria.CONGELADOS -> R.color.light_blue
                Categoria.OTROS -> R.color.gray
            }
            tvCantidad.setBackgroundTintList(itemView.context.getColorStateList(colorRes))
        }
    }
}

class ProductoDiffCallback : DiffUtil.ItemCallback<Producto>() {
    override fun areItemsTheSame(oldItem: Producto, newItem: Producto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Producto, newItem: Producto): Boolean {
        return oldItem == newItem
    }
} 