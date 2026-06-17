package com.application.al_safar.domain.usecase.notification

import android.os.Build
import com.application.al_safar.data.local.preferences.PreferencesManager
import com.application.al_safar.data.remote.api.NotificationApi
import com.application.al_safar.data.remote.dto.DeviceTokenRegisterRequest
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class RegisterFcmTokenUseCase @Inject constructor(
    private val notificationApi: NotificationApi,
    private val preferencesManager: PreferencesManager
){
    suspend operator fun invoke() : Result<Unit> = runCatching {
        val fcmToken = preferencesManager.fcmToken.first()
            ?: return@runCatching

        notificationApi.registerDeviceToken(
            DeviceTokenRegisterRequest(
                fcmToken = fcmToken,
                deviceName = Build.MODEL,
                deviceType = "android"
            )
        )
    }
}