package com.dcchua.gweather.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "weather",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "userId") val userId: Long,
    @ColumnInfo(name = "temperature") val temperature: String,
    @ColumnInfo(name = "weatherCondition") val weatherCondition: String,
    @ColumnInfo(name = "iconId") val iconId: Int,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "sunrise") val sunrise: String,
    @ColumnInfo(name = "sunset") val sunset: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
)
