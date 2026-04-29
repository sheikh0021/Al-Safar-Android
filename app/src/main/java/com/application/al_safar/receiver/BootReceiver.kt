package com.application.al_safar.receiver


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.application.al_safar.worker.NotificationSyncWorker

/**
 * Triggered when device boots or app is updated.
 * Initializes background tasks and FCM token sync.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("BootReceiver", "System boot or app update detected")

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                // Schedule background sync of notifications
                scheduleNotificationSync(context)
            }
        }
    }

    private fun scheduleNotificationSync(context: Context) {
        val syncRequest = OneTimeWorkRequestBuilder<NotificationSyncWorker>()
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "notification_sync_on_boot",
            androidx.work.ExistingWorkPolicy.KEEP,
            syncRequest
        )

        Log.d("BootReceiver", "Notification sync scheduled")
    }
}