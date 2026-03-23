package com.application.al_safar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.application.al_safar.data.local.entities.DuaEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface DuaDao {
    @Query("SELECT * FROM duas ORDER BY title ASC")
    fun getAllDuas(): Flow<List<DuaEntity>>

    @Query("SELECT * FROM duas WHERE id = :duaId")
    fun getDuaById(duaId: String): Flow<DuaEntity?>

    @Query("SELECT * FROM duas WHERE category = :category ORDER BY title ASC")
    fun getDuasByCategory(category: String): Flow<List<DuaEntity>>

    @Query("SELECT * FROM duas WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteDuas(): Flow<List<DuaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDua(dua: DuaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDuas(duas: List<DuaEntity>)

    @Update
    suspend fun updateDua(dua: DuaEntity)

    @Query("UPDATE duas SET isFavorite = :isFavorite WHERE id = :duaId")
    suspend fun updateFavoriteStatus(duaId: String, isFavorite: Boolean)

    @Query("DELETE FROM duas")
    suspend fun clearAll()
}