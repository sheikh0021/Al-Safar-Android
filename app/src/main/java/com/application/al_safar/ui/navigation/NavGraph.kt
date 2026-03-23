package com.application.al_safar.ui.navigation

import androidx.compose.runtime.Composable
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
import com.application.al_safar.ui.booking.UploadDocumentsScreen
import com.application.al_safar.ui.guide.DuaCollectionScreen
import com.application.al_safar.ui.guide.UmrahGuideScreen
import com.application.al_safar.ui.guide.UmrahStepDetailScreen
import com.application.al_safar.ui.home.HomeScreen
import com.application.al_safar.ui.language.LanguageScreen
import com.application.al_safar.ui.map.MapScreen
import com.application.al_safar.ui.packagedetail.PackageDetailScreen
import com.application.al_safar.ui.splash.SplashScreen
import com.application.al_safar.ui.support.SupportScreen
import com.application.al_safar.ui.trip.TripDashboardScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screen.Language.route) {
            LanguageScreen(navController = navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignup = { navController.navigate(Screen.Signup.route) }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
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
                onLogout = {
                    navController.navigate(Screen.Login.route){
                        popUpTo(Screen.Home.route) {inclusive = true}
                    }
                }
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
    }
}
