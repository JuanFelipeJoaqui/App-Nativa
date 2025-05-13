package com.example.chopchop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chopchop.R
import com.example.chopchop.adapters.ProductosAdapter
import com.example.chopchop.models.Producto
import com.example.chopchop.viewmodels.ListasViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText

class ListasFragment : Fragment() {
    private val viewModel: ListasViewModel by viewModels()
    private lateinit var adapter: ProductosAdapter
    private lateinit var rvProductos: RecyclerView
    private lateinit var etBuscarProducto: TextInputEditText
    private lateinit var btnGuardar: Button
    private lateinit var etNombreLista: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_listas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        inicializarVistas(view)
        configurarRecyclerView()
        configurarObservadores()
        configurarListeners()
    }

    private fun inicializarVistas(view: View) {
        rvProductos = view.findViewById(R.id.rvProductos)
        etBuscarProducto = view.findViewById(R.id.etBuscarProducto)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        etNombreLista = view.findViewById(R.id.etNombreLista)
    }

    private fun configurarRecyclerView() {
        adapter = ProductosAdapter(
            onAgregarClick = { producto ->
                viewModel.actualizarCantidadProducto(
                    viewModel.listaActual.value?.id ?: return@ProductosAdapter,
                    producto.id,
                    producto.cantidad + 1
                )
            },
            onQuitarClick = { producto ->
                if (producto.cantidad > 1) {
                    viewModel.actualizarCantidadProducto(
                        viewModel.listaActual.value?.id ?: return@ProductosAdapter,
                        producto.id,
                        producto.cantidad - 1
                    )
                }
            },
            onEliminarClick = { producto ->
                viewModel.eliminarProducto(
                    viewModel.listaActual.value?.id ?: return@ProductosAdapter,
                    producto.id
                )
            }
        )

        rvProductos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ListasFragment.adapter
        }
    }

    private fun configurarObservadores() {
        viewModel.listaActual.observe(viewLifecycleOwner) { lista ->
            adapter.submitList(lista.productos)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun configurarListeners() {
        btnGuardar.setOnClickListener {
            val nombreLista = etNombreLista.text.toString()
            if (nombreLista.isNotEmpty()) {
                viewModel.crearLista(nombreLista)
                etNombreLista.text.clear()
            } else {
                Toast.makeText(context, "Ingresa un nombre para la lista", Toast.LENGTH_SHORT).show()
            }
        }

        etBuscarProducto.setOnEditorActionListener { _, _, _ ->
            val nombreProducto = etBuscarProducto.text.toString()
            if (nombreProducto.isNotEmpty()) {
                agregarProducto(nombreProducto)
                etBuscarProducto.text?.clear()
            }
            true
        }
    }

    private fun agregarProducto(nombre: String) {
        val producto = Producto(
            id = System.currentTimeMillis().toString(),
            nombre = nombre
        )
        viewModel.agregarProducto(
            viewModel.listaActual.value?.id ?: return,
            producto
        )
    }

    private fun mostrarOpcionesLista() {
        val bottomSheet = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_opciones_lista, null)
        bottomSheet.setContentView(view)

        view.findViewById<View>(R.id.btnAnadirFavoritos).setOnClickListener {
            viewModel.toggleFavorito(viewModel.listaActual.value?.id ?: return@setOnClickListener)
            bottomSheet.dismiss()
        }

        view.findViewById<View>(R.id.btnDesmarcarProductos).setOnClickListener {
            viewModel.desmarcarTodosLosProductos(viewModel.listaActual.value?.id ?: return@setOnClickListener)
            bottomSheet.dismiss()
        }

        view.findViewById<View>(R.id.btnVaciarLista).setOnClickListener {
            viewModel.vaciarLista(viewModel.listaActual.value?.id ?: return@setOnClickListener)
            bottomSheet.dismiss()
        }

        view.findViewById<Button>(R.id.btnEliminarLista).setOnClickListener {
            viewModel.eliminarLista(viewModel.listaActual.value?.id ?: return@setOnClickListener)
            bottomSheet.dismiss()
        }

        bottomSheet.show()
    }
} 