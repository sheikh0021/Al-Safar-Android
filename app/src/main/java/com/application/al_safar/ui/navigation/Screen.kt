package com.application.al_safar.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Language : Screen("language")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
    object UmrahGuide : Screen("umrah_guide")
    object DuaCollection : Screen("dua_collection")
    object TripDashboard : Screen("trip_dashboard")
    object Support : Screen("support")
    object Map : Screen("map")
    object AiChatbot : Screen("ai_chatbot")
    object GuideDashboard : Screen("guide_dashboard")
    object MyGuide : Screen("my_guide")
    object NotificationCenter : Screen("notification_center")
    object NotificationPreferences : Screen("notification_preferences")

    object PackageDetail : Screen("package_detail/{packageId}") {
        fun createRoute(packageId: String) = "package_detail/$packageId"
    }

    object BookingDateGroup : Screen("booking_date_group/{packageId}") {
        fun createRoute(packageId: String) = "booking_date_group/$packageId"
    }

    object UploadDocuments : Screen("upload_documents/{bookingId}") {
        fun createRoute(bookingId: String) = "upload_documents/$bookingId"
    }

    object Payment : Screen("payment/{bookingId}") {
        fun createRoute(bookingId: String) = "payment/$bookingId"
    }

    object Confirmation : Screen("confirmation/{bookingId}") {
        fun createRoute(bookingId: String) = "confirmation/$bookingId"
    }

    object UmrahStepDetail : Screen("umrah_step_detail/{stepId}") {
        fun createRoute(stepId: String) = "umrah_step_detail/$stepId"
    }
    object TravelerChat : Screen("traveler_chat/{assignmentId}") {
        fun createRoute(assignmentId: String) = "traveler_chat/$assignmentId"
    }
    object GuideChatDetail : Screen("guide_chat_detail/{assignmentId}"){
        fun createRoute(assignmentId: String) = "guide_chat_detail/$assignmentId"
    }
}
