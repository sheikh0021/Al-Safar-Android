package com.application.al_safar.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.application.al_safar.data.local.entities.GroupEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface GroupDao {
    @Query("SELECT * FROM `groups` WHERE leaderId = :userId OR memberIds LIKE '%' || :userId || '%'")
    fun getGroupsByUserId(userId: String): Flow<List<GroupEntity>>

    @Query("SELECT * FROM `groups` WHERE id = :groupId")
    fun getGroupById(groupId: String): Flow<GroupEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity)

    @Update
    suspend fun updateGroup(group: GroupEntity)

    @Delete
    suspend fun deleteGroup(group: GroupEntity)
}