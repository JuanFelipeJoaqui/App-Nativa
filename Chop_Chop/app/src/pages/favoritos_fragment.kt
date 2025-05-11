package com.example.chopchop.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chopchop.R

class FavoritosFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Crea un layout XML para favoritos si lo deseas
        return inflater.inflate(R.layout.favoritos_fragment, container, false)
    }
}
