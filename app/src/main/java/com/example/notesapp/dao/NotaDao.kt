package com.example.notesapp.dao


import androidx.lifecycle.liveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notesapp.entities.NotasEntity

@Dao
interface NotaDao {

    @Query("SELECT * FROM table_notas ORDER BY autor DESC")
    suspend fun getAllNotas(): List<NotasEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNota(nota: NotasEntity)
}