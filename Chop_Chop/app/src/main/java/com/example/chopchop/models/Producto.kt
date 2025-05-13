package com.example.chopchop.models

data class Producto(
    val id: String = "",
    var nombre: String = "",
    var cantidad: Int = 1,
    var categoria: Categoria = Categoria.OTROS,
    var completado: Boolean = false
)

enum class Categoria {
    FRUTAS_VERDURAS,
    CARNES,
    LACTEOS,
    PANADERIA,
    BEBIDAS,
    LIMPIEZA,
    DESPENSA,
    CONGELADOS,
    OTROS
} 