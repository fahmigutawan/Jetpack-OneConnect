package com.example.oneconnect.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.oneconnect.model.entity.FavoriteItemEntity

@Database(
    entities = [
        FavoriteItemEntity::class
    ],
    version = 1
)
abstract class RoomDb: RoomDatabase() {

}