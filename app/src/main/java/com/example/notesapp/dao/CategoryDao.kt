package com.example.notesapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.notesapp.entities.CategoryEntity

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM Category")
    suspend fun getAllCategories(): List<CategoryEntity>
}