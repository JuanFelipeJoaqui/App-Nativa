package com.example.chopchop.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chopchop.R

class AjustesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Crea un layout XML para ajustes si lo deseas
        return inflater.inflate(R.layout.ajustes_fragment, container, false)
    }
}
