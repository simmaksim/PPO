package com.example.lr2.data

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class TabataEntity(
    var name: String,
    var color: String,
    var warm_up: Int,
    var work: Int,
    var rest: Int,
    var repeats: Int,
    var cycles: Int,
    var cooldown:Int
) : Serializable
{
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}