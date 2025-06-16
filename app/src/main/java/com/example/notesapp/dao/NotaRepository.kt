package com.example.notesapp.dao

import com.example.notesapp.entities.NotasEntity


class NotaRepository(private val notaDao: NotaDao) {

    suspend fun insertar(nota : NotasEntity) {
        notaDao.insertNota(nota)
    }
}