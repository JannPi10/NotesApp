package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.notesapp.dao.NotaRepository
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.entities.CategoryEntity
import com.example.notesapp.entities.NotasEntity
import com.example.notesapp.utils.CryptoUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.notesapp.PrimerFragment

class MainActivity : AppCompatActivity() {

    //DECLARAMOS LAS VARIABLES QUE USAREMOS
    private lateinit var repository: NotaRepository
    private lateinit var db : NoteDatabase
    private lateinit var spinner: Spinner
    private var categories : List<CategoryEntity> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //COLOCAMOS NUESTRO FRAGMENTO
        supportFragmentManager.commit {
            replace<PrimerFragment>(R.id.linearContainer)
            setReorderingAllowed(true)
            addToBackStack("replacement")
        }

        db = NoteDatabase.getDatabase(this)
        //repository = NotaRepository(NoteDatabase.getDatabase(this).notaDao)
        repository = NotaRepository(db.notaDao)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val editTextTitulo = findViewById<EditText>(R.id.editTextTitulo)
        val editTextAutor = findViewById<EditText>(R.id.editTextAutor)

        //ACA PASAMOS A OTRA ACTIVIDAD SI SE OPRIMEN EL BOTON
        val btnVerNotas = findViewById<Button>(R.id.btnVerNotas)
        spinner = findViewById(R.id.spinnerCategoria)

        // Aquí agregas las categorías por defecto
        lifecycleScope.launch(Dispatchers.IO) {
            val dao = db.CategoryDao
            if (dao.getAllCategories().isEmpty()) {
                listOf("Trabajo", "Escuela", "Personal").forEach {
                    dao.insertCategory(CategoryEntity(category = CryptoUtils.encrypt(it)))
                }
            }
        }

        //  Luego cargamos el Spinner

        lifecycleScope.launch(Dispatchers.IO) {
            categories = db.CategoryDao.getAllCategories()
            val nombres = categories.map {
                val decrypted = CryptoUtils.decrypt(it.category)
                Log.d("SPINNER", "Desencriptando categoría: $decrypted")
                decrypted
            }

            withContext(Dispatchers.Main){
                val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, nombres)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }

        btnLogin.setOnClickListener {
            val titulo = editTextTitulo.text.toString()
            val autor = editTextAutor.text.toString()
            val categoria = categories.getOrNull(spinner.selectedItemPosition)

            if (titulo.isNotEmpty() && autor.isNotEmpty() && categoria != null) {
                insertarNota(titulo, autor, categoria.id)
                Toast.makeText(this, "Nota Guardada correctamente", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_LONG).show()
            }
        }

        btnVerNotas.setOnClickListener {
            val intent = Intent(this, VerNotasActivity::class.java)
            startActivity(intent)
        }
    }


    private fun insertarNota(titulo: String, autor: String, id: Int) {
        val nuevaNota = NotasEntity(
            titulo = CryptoUtils.encrypt(titulo),
            autor = CryptoUtils.encrypt(autor),
            category_id = id
        )

        lifecycleScope.launch(Dispatchers.IO) {
            repository.insertar(nuevaNota)
        }

    }
}

