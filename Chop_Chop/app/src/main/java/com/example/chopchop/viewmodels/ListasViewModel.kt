package com.example.chopchop.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chopchop.models.Lista
import com.example.chopchop.models.Producto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

class ListasViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    
    private val _listas = MutableLiveData<List<Lista>>()
    val listas: LiveData<List<Lista>> = _listas
    
    private val _listaActual = MutableLiveData<Lista>()
    val listaActual: LiveData<Lista> = _listaActual
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        cargarListas()
    }

    fun cargarListas() = viewModelScope.launch {
        try {
            val userId = auth.currentUser?.uid ?: return@launch
            val snapshot = db.collection("listas")
                .whereEqualTo("usuarioId", userId)
                .get()
                .await()
            
            _listas.value = snapshot.toObjects(Lista::class.java)
        } catch (e: Exception) {
            _error.value = "Error al cargar las listas: ${e.message}"
        }
    }

    fun crearLista(nombre: String) = viewModelScope.launch {
        try {
            val userId = auth.currentUser?.uid ?: return@launch
            val nuevaLista = Lista(
                nombre = nombre,
                usuarioId = userId
            )
            
            db.collection("listas")
                .add(nuevaLista)
                .await()
            
            cargarListas()
        } catch (e: Exception) {
            _error.value = "Error al crear la lista: ${e.message}"
        }
    }

    fun agregarProducto(listaId: String, producto: Producto) = viewModelScope.launch {
        try {
            db.collection("listas").document(listaId)
                .update("productos", producto)
                .await()
            
            cargarListas()
        } catch (e: Exception) {
            _error.value = "Error al agregar el producto: ${e.message}"
        }
    }

    fun actualizarCantidadProducto(listaId: String, productoId: String, cantidad: Int) = viewModelScope.launch {
        try {
            val lista = _listaActual.value ?: return@launch
            val producto = lista.productos.find { it.id == productoId } ?: return@launch
            producto.cantidad = cantidad
            
            db.collection("listas").document(listaId)
                .update("productos", lista.productos)
                .await()
            
            _listaActual.value = lista
        } catch (e: Exception) {
            _error.value = "Error al actualizar la cantidad: ${e.message}"
        }
    }

    fun eliminarProducto(listaId: String, productoId: String) = viewModelScope.launch {
        try {
            val lista = _listaActual.value ?: return@launch
            lista.productos.removeAll { it.id == productoId }
            
            db.collection("listas").document(listaId)
                .update("productos", lista.productos)
                .await()
            
            _listaActual.value = lista
        } catch (e: Exception) {
            _error.value = "Error al eliminar el producto: ${e.message}"
        }
    }

    fun toggleFavorito(listaId: String) = viewModelScope.launch {
        try {
            val lista = _listaActual.value ?: return@launch
            lista.esFavorita = !lista.esFavorita
            
            db.collection("listas").document(listaId)
                .update("esFavorita", lista.esFavorita)
                .await()
            
            _listaActual.value = lista
        } catch (e: Exception) {
            _error.value = "Error al actualizar favoritos: ${e.message}"
        }
    }

    fun compartirLista(listaId: String, emailUsuario: String) = viewModelScope.launch {
        try {
            val userSnapshot = db.collection("usuarios")
                .whereEqualTo("email", emailUsuario)
                .get()
                .await()
            
            val userId = userSnapshot.documents.firstOrNull()?.id ?: throw Exception("Usuario no encontrado")
            
            val lista = _listaActual.value ?: return@launch
            if (!lista.compartidoCon.contains(userId)) {
                lista.compartidoCon = lista.compartidoCon + userId
                
                db.collection("listas").document(listaId)
                    .update("compartidoCon", lista.compartidoCon)
                    .await()
                
                _listaActual.value = lista
            }
        } catch (e: Exception) {
            _error.value = "Error al compartir la lista: ${e.message}"
        }
    }

    fun vaciarLista(listaId: String) = viewModelScope.launch {
        try {
            db.collection("listas").document(listaId)
                .update(
                    mapOf(
                        "productos" to listOf<Producto>(),
                        "ultimaModificacion" to Date()
                    )
                )
                .await()
            
            val lista = _listaActual.value
            if (lista != null) {
                lista.productos.clear()
                lista.ultimaModificacion = Date()
                _listaActual.value = lista
            }
        } catch (e: Exception) {
            _error.value = "Error al vaciar la lista: ${e.message}"
        }
    }

    fun eliminarLista(listaId: String) = viewModelScope.launch {
        try {
            db.collection("listas").document(listaId)
                .delete()
                .await()
            
            cargarListas()
        } catch (e: Exception) {
            _error.value = "Error al eliminar la lista: ${e.message}"
        }
    }

    fun desmarcarTodosLosProductos(listaId: String) = viewModelScope.launch {
        try {
            val lista = _listaActual.value ?: return@launch
            lista.productos.forEach { it.completado = false }
            
            db.collection("listas").document(listaId)
                .update("productos", lista.productos)
                .await()
            
            _listaActual.value = lista
        } catch (e: Exception) {
            _error.value = "Error al desmarcar los productos: ${e.message}"
        }
    }
} 