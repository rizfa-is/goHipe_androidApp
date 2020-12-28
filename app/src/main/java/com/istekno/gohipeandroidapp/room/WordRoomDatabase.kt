package com.istekno.gohipeandroidapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WordRoomEntity::class], version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao() : WordRoomDAO

    companion object {

        private var INSTANCE: WordRoomDatabase? = null

        fun getWordDatabase(context: Context) : WordRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    WordRoomDatabase::class.java, "word_room.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}