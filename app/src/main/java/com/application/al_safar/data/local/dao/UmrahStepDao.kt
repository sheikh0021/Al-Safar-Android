package com.application.al_safar.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.application.al_safar.data.local.entities.UmrahStepEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UmrahStepDao {
    @Query("SELECT * FROM umrah_steps ORDER BY stepNumber ASC")
    fun getAllSteps(): Flow<List<UmrahStepEntity>>

    @Query("SELECT * FROM umrah_steps WHERE id = :stepId")
    fun getStepById(stepId: String): Flow<UmrahStepEntity?>

    @Query("SELECT * FROM umrah_steps WHERE category = :category ORDER BY stepNumber ASC")
    fun getStepsByCategory(category: String): Flow<List<UmrahStepEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(step: UmrahStepEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: List<UmrahStepEntity>)

    @Update
    suspend fun updateStep(step: UmrahStepEntity)

    @Query("UPDATE umrah_steps SET isCompleted = :isCompleted WHERE id = :stepId")
    suspend fun updateStepCompletion(stepId: String, isCompleted: Boolean)

    @Query("DELETE FROM umrah_steps")
    suspend fun clearAll()
}