package com.example.notesapp.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.notesapp.dao.CategoryDao
import com.example.notesapp.dao.NotaDao
import com.example.notesapp.entities.CategoryEntity
import com.example.notesapp.entities.NotasEntity
import com.example.notesapp.utils.CryptoUtils

@Database(entities = [NotasEntity::class, CategoryEntity::class], version = 2)
abstract class NoteDatabase : RoomDatabase() {
    abstract val notaDao: NotaDao
    abstract val CategoryDao: CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}