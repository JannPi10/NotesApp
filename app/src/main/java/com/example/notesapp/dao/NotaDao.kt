package com.example.notesapp.dao


import androidx.lifecycle.liveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notesapp.entities.NotasEntity

@Dao
interface NotaDao {

    @Query("SELECT * FROM table_notas ORDER BY autor DESC")
    suspend fun getAllNotas(): List<NotasEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNota(nota: NotasEntity)

    @Delete
    suspend fun deleteNota(nota: NotasEntity)

    @Query("SELECT * FROM table_notas WHERE id = :id LIMIT 1")
    fun getNotaPorId(id : Int) : NotasEntity

    @Update
    fun updateNota(nota: NotasEntity)
}