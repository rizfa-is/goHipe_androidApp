package com.istekno.gohipeandroidapp.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
data class WordRoomEntity(var name: String = "",
                            @ColumnInfo(name = "created_at") var createdAt: Long = 0) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}