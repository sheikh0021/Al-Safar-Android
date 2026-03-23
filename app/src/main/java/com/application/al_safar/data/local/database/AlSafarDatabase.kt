package com.application.al_safar.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.application.al_safar.data.local.dao.DuaDao
import com.application.al_safar.data.local.dao.GroupDao
import com.application.al_safar.data.local.dao.PlaceDao
import com.application.al_safar.data.local.dao.TripDao
import com.application.al_safar.data.local.dao.UmrahStepDao
import com.application.al_safar.data.local.dao.UserDao
import com.application.al_safar.data.local.entities.DuaEntity
import com.application.al_safar.data.local.entities.GroupEntity
import com.application.al_safar.data.local.entities.PlaceEntity
import com.application.al_safar.data.local.entities.TripEntity
import com.application.al_safar.data.local.entities.UmrahStepEntity
import com.application.al_safar.data.local.entities.UserEntity


@Database(
    entities = [
        UserEntity::class,
        UmrahStepEntity::class,
        DuaEntity::class,
        TripEntity::class,
        PlaceEntity::class,
        GroupEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AlSafarDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun umrahStepDao(): UmrahStepDao
    abstract fun duaDao(): DuaDao
    abstract fun tripDao(): TripDao
    abstract fun placeDao(): PlaceDao
    abstract fun groupDao(): GroupDao
}