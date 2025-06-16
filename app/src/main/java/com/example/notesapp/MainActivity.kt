package com.example.notesapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.notesapp.dao.NotaRepository
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.entities.NotasEntity
import com.example.notesapp.ui.theme.NotesAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private lateinit var repository: NotaRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = NotaRepository(NoteDatabase.getDatabase(this).notaDao)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val editTextTitulo = findViewById<EditText>(R.id.editTextTitulo)
        val editTextAutor = findViewById<EditText>(R.id.editTextAutor)

        btnLogin.setOnClickListener {
            val titulo = editTextTitulo.text.toString()
            val autor = editTextAutor.text.toString()

            if (titulo.isNotEmpty() && autor.isNotEmpty()) {
                insertarNota(titulo, autor)
                Toast.makeText(this, "Nota Guardada correctamente", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun insertarNota(titulo: String, autor: String) {

        val nuevaNota = NotasEntity(
            titulo = titulo,
            autor = autor
        )

        lifecycleScope.launch(Dispatchers.IO) {
            repository.insertar(nuevaNota)
        }

    }
}

