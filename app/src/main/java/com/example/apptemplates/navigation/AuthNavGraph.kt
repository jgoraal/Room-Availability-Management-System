package com.example.apptemplates.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.apptemplates.presentation.login.login_menu.AuthViewModel
import com.example.apptemplates.presentation.login.login_menu.LoginMenuScreen
import com.example.apptemplates.presentation.login.sign_in.SignInScreen
import com.example.apptemplates.presentation.login.sign_in.SignInViewModel
import com.example.apptemplates.presentation.login.sign_up.SignUpScreen
import com.example.apptemplates.presentation.login.sign_up.SignUpViewModel
import com.example.apptemplates.presentation.login.sign_up_confirm.SignUpConfirmScreen
import com.example.apptemplates.presentation.login.sign_up_confirm.SignUpConfirmViewModel
import com.example.apptemplates.presentation.login.temp.SignUpConfirmEmailScreen
import com.example.apptemplates.presentation.login.test.TestScreen
import com.example.apptemplates.presentation.login.test.TestViewModel


fun NavGraphBuilder.authNavGraph(
    navController: NavController
) {
    navigation(
        route = AppScreen.Auth.route,
        startDestination = AppScreen.Auth.LoginMenu.route
    ) {

        composable(
            route = AppScreen.Auth.LoginMenu.route
        ) {

            val authViewModel: AuthViewModel =
                viewModel()

            // Collect the authentication state
            val isUserAuthenticated by authViewModel.isUserAuthenticated.collectAsState()


            if (!isUserAuthenticated) {
                navController.navigate(AppScreen.Auth.SignIn.route) {
                    popUpTo(AppScreen.Auth.route) { inclusive = true }
                }
            } else {
                // If not authenticated, display the login screen

                val testViewModel: TestViewModel = viewModel()

                TestScreen(
                    viewModel = testViewModel
                )


                /*LoginMenuScreen(
                    viewModel = authViewModel,
                    navigateToSignIn = {
                        navController.navigate(AppScreen.Auth.SignIn.route)
                    },
                    navigateToSignUp = {
                        navController.navigate(AppScreen.Auth.SignUp.route)
                    }
                )*/
            }
        }


        composable(
            route = AppScreen.Auth.SignIn.route
        ) {
            val signInViewModel: SignInViewModel =
                viewModel()

            SignInScreen(
                viewModel = signInViewModel,
                navigateToHome = {
                    navController.navigate(AppScreen.Main.route) {
                        popUpTo(AppScreen.Auth.route) {
                            inclusive = true
                        }
                    }
                },

                navigateBack = {
                    navController.navigateUp()
                },

                navigateToReset = {
                    navController.navigate(AppScreen.Auth.ResetPassword.route)
                }
            )
        }


        composable(
            route = AppScreen.Auth.SignUp.route
        ) {

            val signUpViewModel: SignUpViewModel =
                viewModel(navController.getBackStackEntry(AppScreen.Auth.route))
            SignUpScreen(
                viewModel = signUpViewModel,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateConfirm = {
                    navController.navigate(AppScreen.Auth.SignUpConfirm.route)
                }
            )
        }

        composable(
            route = AppScreen.Auth.SignUpConfirm.route
        ) {
            val signUpConfirmViewModel: SignUpConfirmViewModel =
                viewModel(navController.getBackStackEntry(AppScreen.Auth.route))
            SignUpConfirmScreen(
                viewModel = signUpConfirmViewModel,
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateConfirm = {
                    navController.navigate(AppScreen.Auth.SignUpConfirmEmail.route)
                }
            )
        }


        composable(
            route = AppScreen.Auth.SignUpConfirmEmail.route
        ) {
            val signUpViewModel: SignUpViewModel =
                viewModel(navController.getBackStackEntry(AppScreen.Auth.route))
            SignUpConfirmEmailScreen(
                viewModel = signUpViewModel
            )
        }
    }
}