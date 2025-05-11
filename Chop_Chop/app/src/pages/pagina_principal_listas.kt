package com.example.chopchop.pages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.chopchop.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class PaginaPrincipalListas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pagina_principal_listas)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_listas -> {
                    loadFragment(ListasFragment())
                    true
                }
                R.id.nav_favoritos -> {
                    loadFragment(FavoritosFragment())
                    true
                }
                R.id.nav_ajustes -> {
                    loadFragment(AjustesFragment())
                    true
                }
                else -> false
            }
        }
        // Cargar fragmento por defecto
        bottomNav.selectedItemId = R.id.nav_listas
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}