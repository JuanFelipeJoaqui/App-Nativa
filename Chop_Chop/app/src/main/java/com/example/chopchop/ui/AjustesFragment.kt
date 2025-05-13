package com.example.chopchop.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.chopchop.R
import com.example.chopchop.pages.LoginPage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.EmailAuthProvider

class AjustesFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var tvSaludo: TextView
    private lateinit var switchModoOscuro: SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ajustes_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        
        // Inicializar vistas
        tvSaludo = view.findViewById(R.id.tvSaludo)
        switchModoOscuro = view.findViewById(R.id.switchModoOscuro)
        
        // Configurar nombre de usuario
        auth.currentUser?.let { user ->
            val nombre = user.displayName ?: user.email?.substringBefore("@") ?: "Usuario"
            tvSaludo.text = "¡Hola, $nombre!"
        }

        // Configurar modo oscuro
        switchModoOscuro.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        view.findViewById<View>(R.id.btnModoOscuro).setOnClickListener {
            switchModoOscuro.isChecked = !switchModoOscuro.isChecked
            toggleDarkMode()
        }
        switchModoOscuro.setOnCheckedChangeListener { _, isChecked ->
            toggleDarkMode()
        }

        // Configurar cambio de contraseña
        view.findViewById<View>(R.id.btnCambiarContrasena).setOnClickListener {
            mostrarDialogoCambioContrasena()
        }

        // Configurar ver créditos
        view.findViewById<View>(R.id.btnVerCreditos).setOnClickListener {
            mostrarDialogoCreditos()
        }

        // Configurar enviar comentarios
        view.findViewById<View>(R.id.btnEnviarComentarios).setOnClickListener {
            mostrarDialogoComentarios()
        }

        // Configurar cerrar sesión
        view.findViewById<Button>(R.id.btnCerrarSesion).setOnClickListener {
            cerrarSesion()
        }
    }

    private fun toggleDarkMode() {
        val modo = if (switchModoOscuro.isChecked) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(modo)
    }

    private fun mostrarDialogoCambioContrasena() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_cambiar_contrasena, null)
        val contrasenaActual = dialogView.findViewById<EditText>(R.id.etContrasenaActual)
        val contrasenaNueva = dialogView.findViewById<EditText>(R.id.etContrasenaNueva)
        val confirmacionContrasena = dialogView.findViewById<EditText>(R.id.etConfirmarContrasena)

        AlertDialog.Builder(requireContext())
            .setTitle("Cambiar Contraseña")
            .setView(dialogView)
            .setPositiveButton("Cambiar") { dialog, _ ->
                val currentPass = contrasenaActual.text.toString()
                val newPass = contrasenaNueva.text.toString()
                val confirmPass = confirmacionContrasena.text.toString()

                if (newPass == confirmPass) {
                    val user = auth.currentUser
                    if (user != null && user.email != null) {
                        val credential = EmailAuthProvider.getCredential(user.email!!, currentPass)
                        user.reauthenticate(credential).addOnCompleteListener { reauth ->
                            if (reauth.isSuccessful) {
                                user.updatePassword(newPass).addOnCompleteListener { update ->
                                    if (update.isSuccessful) {
                                        Toast.makeText(context, "Contraseña actualizada exitosamente", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "La contraseña actual es incorrecta", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoCreditos() {
        AlertDialog.Builder(requireContext())
            .setTitle("Créditos")
            .setMessage("""
                Chop Chop v1.0
                
                Desarrollado por:
                - Equipo Chop Chop
                
                Diseño UI/UX:
                - Figma Team
                
                © 2024 Chop Chop. Todos los derechos reservados.
            """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun mostrarDialogoComentarios() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_comentarios, null)
        val comentario = dialogView.findViewById<EditText>(R.id.etComentario)

        AlertDialog.Builder(requireContext())
            .setTitle("Enviar Comentarios")
            .setView(dialogView)
            .setPositiveButton("Enviar") { _, _ ->
                // Aquí implementarías la lógica para enviar el comentario
                Toast.makeText(context, "¡Gracias por tus comentarios!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cerrarSesion() {
        AlertDialog.Builder(requireContext())
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                auth.signOut()
                startActivity(Intent(requireContext(), LoginPage::class.java))
                requireActivity().finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
} 