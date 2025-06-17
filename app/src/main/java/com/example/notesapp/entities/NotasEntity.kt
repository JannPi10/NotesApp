package com.example.notesapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_notas")
data class NotasEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "titulo") val titulo: String,
    @ColumnInfo(name = "autor") val autor: String,
    @ColumnInfo(name = "Category_id") val category_id: Int
)