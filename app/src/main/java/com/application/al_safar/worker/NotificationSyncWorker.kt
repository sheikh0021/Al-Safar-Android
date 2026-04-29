package com.application.al_safar.worker


import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.hilt.work.HiltWorker
import com.application.al_safar.data.local.database.AlSafarDatabase
import com.application.al_safar.data.remote.api.NotificationApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Background worker that syncs notifications from backend periodically.
 * Runs even if app is not open.
 */
@HiltWorker
class NotificationSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val database: AlSafarDatabase,
    private val api: NotificationApi,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            Log.d("NotificationSyncWorker", "Starting notification sync")

            // Fetch recent notifications from backend
            val response = api.getRecentNotifications(limit = 50)

            if (response.isSuccessful) {
                val notifications = response.body()
                Log.d("NotificationSyncWorker", "Fetched ${notifications?.size} notifications")

                // Could save to local DB here if needed
                Result.success()
            } else {
                Log.w("NotificationSyncWorker", "Failed to fetch notifications: ${response.code()}")
                Result.retry()
            }

        } catch (e: Exception) {
            Log.e("NotificationSyncWorker", "Error syncing notifications", e)
            Result.retry()
        }
    }
}