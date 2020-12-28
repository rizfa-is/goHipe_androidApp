package com.istekno.gohipeandroidapp.room

import androidx.room.*

@Dao
interface WordRoomDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(word: WordRoomEntity)

    @Query("SELECT * FROM word_table ORDER BY name ASC")
    fun getAllWord(): List<WordRoomEntity>

    @Delete
    fun delete(word: WordRoomEntity)

}