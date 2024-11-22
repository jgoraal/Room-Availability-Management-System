package com.example.apptemplates.navigation.nav_graph

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.apptemplates.navigation.route.AppScreen
import com.example.apptemplates.presentation.login.login_menu.LoginMenuScreen
import com.example.apptemplates.presentation.login.password_reset.PasswordResetScreen
import com.example.apptemplates.presentation.login.password_reset.ResetPasswordViewModel
import com.example.apptemplates.presentation.login.sign_in.SignInScreen
import com.example.apptemplates.presentation.login.sign_in.SignInViewModel
import com.example.apptemplates.presentation.login.sign_up.SignUpScreen
import com.example.apptemplates.presentation.login.sign_up.SignUpViewModel
import com.example.apptemplates.presentation.login.sign_up_confirm.SignUpConfirmScreen
import com.example.apptemplates.presentation.login.sign_up_confirm.SignUpConfirmViewModel


fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        route = AppScreen.Auth.route,
        startDestination = AppScreen.Auth.LoginMenu.route
    ) {
        loginMenuComposable(navController)
        signInComposable(navController)
        signUpComposable(navController)
        signUpConfirmComposable(navController)
        resetPasswordComposable(navController)
    }
}

private fun NavGraphBuilder.loginMenuComposable(navController: NavController) {
    composable(route = AppScreen.Auth.LoginMenu.route) {
        /*val authViewModel: AuthViewModel = viewModel()
        val isUserAuthenticated by authViewModel.isUserAuthenticated.collectAsState()

        // Use LaunchedEffect to handle navigation
        LaunchedEffect(isUserAuthenticated) {
            if (isUserAuthenticated) {
                navController.navigate(AppScreen.Main.route) {
                    popUpTo(AppScreen.Auth.route) { inclusive = true }
                }
            }
        }*/

        // Show the login menu screen if the user is not authenticated
        //if (!isUserAuthenticated) {
        LoginMenuScreen(
            navigateToSignIn = { navController.navigate(AppScreen.Auth.SignIn.route) },
            navigateToSignUp = { navController.navigate(AppScreen.Auth.SignUp.route) }
        )
        //}
    }
}


private fun NavGraphBuilder.signInComposable(navController: NavController) {
    composable(route = AppScreen.Auth.SignIn.route) {

        val context = LocalContext.current
        val sharedPreferences =
            context.getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)

        val signInViewModel: SignInViewModel = viewModel(
            factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SignInViewModel(sharedPreferences) as T
                }
            }
        )

        SignInScreen(
            viewModel = signInViewModel,
            navigateToHome = {
                navController.navigate(AppScreen.Main.route) {
                    popUpTo(AppScreen.Auth.route) { inclusive = true }
                }
            },
            navigateBack = { navController.navigateUp() },
            navigateToReset = { navController.navigate(AppScreen.Auth.ResetPassword.route) }
        )
    }
}

private fun NavGraphBuilder.signUpComposable(navController: NavController) {
    composable(route = AppScreen.Auth.SignUp.route) {
        val signUpViewModel: SignUpViewModel =
            viewModel(navController.getBackStackEntry(AppScreen.Auth.route))
        SignUpScreen(
            viewModel = signUpViewModel,
            onNavigateBack = { navController.navigateUp() },
            onNavigateConfirm = { navController.navigate(AppScreen.Auth.SignUpConfirm.route) }
        )
    }
}

private fun NavGraphBuilder.signUpConfirmComposable(navController: NavController) {
    composable(route = AppScreen.Auth.SignUpConfirm.route) {
        val signUpConfirmViewModel: SignUpConfirmViewModel =
            viewModel(/*navController.getBackStackEntry(AppScreen.Auth.route)*/)
        SignUpConfirmScreen(
            viewModel = signUpConfirmViewModel,
            onNavigateBack = { navController.navigateUp() },
            onNavigateConfirm = {
                navController.navigate(AppScreen.Main.route) {
                    popUpTo(AppScreen.Auth.route) { inclusive = true }
                }
            }
        )
    }
}

private fun NavGraphBuilder.resetPasswordComposable(navController: NavController) {
    composable(route = AppScreen.Auth.ResetPassword.route) {
        val resetPasswordViewModel: ResetPasswordViewModel =
            viewModel(navController.getBackStackEntry(AppScreen.Auth.route))
        PasswordResetScreen(
            viewModel = resetPasswordViewModel,
            onConfirm = { navController.navigate(AppScreen.Auth.SignIn.route) },
            onBack = { navController.navigate(AppScreen.Auth.SignIn.route) }
        )
    }
}

@Composable
private inline fun <reified T : ViewModel> createViewModel(context: Context, clazz: Class<T>): T {
    return viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return clazz.getConstructor(SharedPreferences::class.java)
                    .newInstance(
                        context.getSharedPreferences(
                            "my_app_prefs",
                            Context.MODE_PRIVATE
                        )
                    ) as T
            }
        }
    )
}