package com.example.chopchop.models

import java.util.Date

data class Lista(
    val id: String = "",
    var nombre: String = "",
    var productos: MutableList<Producto> = mutableListOf(),
    var esFavorita: Boolean = false,
    val fechaCreacion: Date = Date(),
    var ultimaModificacion: Date = Date(),
    val usuarioId: String = "",
    var compartidoCon: List<String> = listOf() // Lista de IDs de usuarios
) 