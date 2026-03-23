package com.application.al_safar.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.application.al_safar.data.local.entities.TripEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TripDao {
    @Query("SELECT * FROM trips WHERE userId = :userId ORDER BY startDate DESC")
    fun getTripsByUserId(userId: String): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :tripId")
    fun getTripById(tripId: String): Flow<TripEntity?>

    @Query("SELECT * FROM trips WHERE userId = :userId AND isActive = 1 LIMIT 1")
    fun getActiveTrip(userId: String): Flow<TripEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity)

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Delete
    suspend fun deleteTrip(trip: TripEntity)

    @Query("DELETE FROM trips WHERE userId = :userId")
    suspend fun clearUserTrips(userId: String)
}