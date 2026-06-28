package com.application.al_safar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.application.al_safar.ui.auth.LoginScreen
import com.application.al_safar.ui.auth.SignupScreen
import com.application.al_safar.ui.booking.BookingDateGroupScreen
import com.application.al_safar.ui.booking.ConfirmationScreen
import com.application.al_safar.ui.booking.PaymentScreen
import com.application.al_safar.ui.booking.TravelerSummaryScreen
import com.application.al_safar.ui.booking.UploadDocumentsScreen
import com.application.al_safar.ui.chatbot.AiChatbotScreen
import com.application.al_safar.ui.guide.DuaCollectionScreen
import com.application.al_safar.ui.guide.GuideChatScreen
import com.application.al_safar.ui.guide.GuideDashboardScreen
import com.application.al_safar.ui.guide.GuideProfileScreen
import com.application.al_safar.ui.guide.MyGuideScreen
import com.application.al_safar.ui.messages.TravelerMessagesScreen
import com.application.al_safar.ui.guide.UmrahGuideScreen
import com.application.al_safar.ui.guide.UmrahStepDetailScreen
import com.application.al_safar.ui.home.HomeScreen
import com.application.al_safar.ui.language.LanguageScreen
import com.application.al_safar.ui.map.MapScreen
import com.application.al_safar.ui.packagedetail.PackageDetailScreen
import com.application.al_safar.ui.notifications.NotificationPreferencesScreen
import com.application.al_safar.ui.notifications.NotificationCenterScreen
import com.application.al_safar.ui.profile.ProfileScreen
import com.application.al_safar.ui.session.SessionViewModel
import com.application.al_safar.ui.splash.SplashScreen
import com.application.al_safar.ui.support.SupportScreen
import com.application.al_safar.ui.trip.TripDashboardScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String? = null
) {

    val sessionViewModel: SessionViewModel = hiltViewModel()
    val myUserId by sessionViewModel.userId.collectAsState()
    val guideName by sessionViewModel.userName.collectAsState()

    NavHost(
        navController = navController,
        startDestination = startDestination ?: Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screen.Language.route) {
            LanguageScreen(navController = navController)
        }

        composable(Screen.NotificationCenter.route) {
            NotificationCenterScreen(navController = navController)
        }

        composable(Screen.NotificationPreferences.route) {
            NotificationPreferencesScreen(navController = navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                    sessionViewModel.loadSession()
                    if (role == "local_guide") {
                        navController.navigate(Screen.GuideDashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                },
                onNavigateToSignup = { navController.navigate(Screen.Signup.route) }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupSuccess = { role ->
                    sessionViewModel.loadSession()
                    if (role == "local_guide") {
                        navController.navigate(Screen.GuideDashboard.route){
                            popUpTo(Screen.Login.route) {inclusive = true}
                        }
                    } else {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) {inclusive = true}
                        }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                onNavigateToUmrahGuide = { navController.navigate(Screen.UmrahGuide.route) },
                onNavigateToTripDashboard = { navController.navigate(Screen.TripDashboard.route) },
                onNavigateToSupport = { navController.navigate(Screen.Support.route) },
                onNavigateToAiChatbot = { navController.navigate(Screen.AiChatbot.route) },
                onNavigateToMyGuide = { navController.navigate(Screen.MyGuide.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.GuideDashboard.route) {
            GuideDashboardScreen(
                navController = navController,
                guideName = guideName,
                myUserId = myUserId,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.GuideDashboard.route) {inclusive = true}
                    }
                }
            )
        }

        composable(Screen.GuideProfile.route) {
            GuideProfileScreen(
                navController = navController,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.GuideDashboard.route) {inclusive = true}
                    }
                }
            )

        }

        composable(Screen.MyGuide.route) {
            MyGuideScreen(navController = navController)
        }

        composable(Screen.TravelerMessages.route) {
            TravelerMessagesScreen(navController = navController)
        }

        composable(
            route = Screen.TravelerChat.route,
            arguments = listOf(
                navArgument("assignmentId") { type = NavType.StringType },
                navArgument("guideName") {
                    type = NavType.StringType
                    defaultValue = "Your Guide"
                }
            )
        ) { backStackEntry ->
            val guideNameRaw = backStackEntry.arguments?.getString("guideName") ?: "Your Guide"
            val guideName = java.net.URLDecoder.decode(guideNameRaw, "UTF-8")
            GuideChatScreen(
                assignmentId = backStackEntry.arguments?.getString("assignmentId") ?: "",
                isGuide = false,
                myUserId = myUserId,
                otherPersonName = guideName,
                navController = navController
            )
        }

        composable(
            route = Screen.GuideChatDetail.route,
            arguments = listOf(navArgument("assignmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            GuideChatScreen(
                assignmentId = backStackEntry.arguments?.getString("assignmentId") ?: "",
                isGuide = true,
                myUserId = myUserId,
                otherPersonName = "Traveler",
                navController = navController
            )
        }

        composable(
            route = Screen.PackageDetail.route,
            arguments = listOf(navArgument("packageId") { type = NavType.StringType })
        ) { backStackEntry ->
            PackageDetailScreen(
                packageId = backStackEntry.arguments?.getString("packageId") ?: "",
                navController = navController
            )
        }

        composable(
            route = Screen.BookingDateGroup.route,
            arguments = listOf(navArgument("packageId") { type = NavType.StringType })
        ) { backStackEntry ->
            BookingDateGroupScreen(
                packageId = backStackEntry.arguments?.getString("packageId") ?: "",
                navController = navController
            )
        }

        composable(
            route = Screen.UploadDocuments.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            UploadDocumentsScreen(
                bookingId = backStackEntry.arguments?.getString("bookingId") ?: "",
                navController = navController
            )
        }

        composable(
            route = Screen.Payment.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            PaymentScreen(
                bookingId = backStackEntry.arguments?.getString("bookingId") ?: "",
                navController = navController
            )
        }

        composable(
            route = Screen.Confirmation.route,
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
        ) { backStackEntry ->
            ConfirmationScreen(
                bookingId = backStackEntry.arguments?.getString("bookingId") ?: "",
                navController = navController
            )
        }

        composable(Screen.TripDashboard.route) {
            TripDashboardScreen(navController = navController)
        }

        composable(Screen.UmrahGuide.route) {
            UmrahGuideScreen(
                onStepClick = { stepId ->
                    navController.navigate(Screen.UmrahStepDetail.createRoute(stepId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.UmrahStepDetail.route,
            arguments = listOf(navArgument("stepId") { type = NavType.StringType })
        ) { backStackEntry ->
            UmrahStepDetailScreen(
                stepId = backStackEntry.arguments?.getString("stepId") ?: "",
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.DuaCollection.route) {
            DuaCollectionScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Support.route) {
            SupportScreen(navController = navController)
        }

        composable(Screen.Map.route) {
            MapScreen()
        }

        composable(Screen.AiChatbot.route) {
            AiChatbotScreen(navController = navController)
        }
        composable(route = Screen.TravelerSummary.route,
            arguments = listOf(navArgument("bookingId"){type = NavType.StringType})
        ) { backStackEntry ->
            TravelerSummaryScreen(
                bookingId = backStackEntry.arguments?.getString("bookingId") ?: "",
                navController = navController
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) {inclusive = true}
                    }
                }
            )
        }

    }
}
