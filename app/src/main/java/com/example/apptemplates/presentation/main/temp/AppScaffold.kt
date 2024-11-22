package com.example.apptemplates.presentation.main.temp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.apptemplates.R
import com.example.apptemplates.TopBarPreview
import com.example.apptemplates.data.reservation.RecurrenceFrequency
import com.example.apptemplates.data.reservation.Reservation
import com.example.apptemplates.data.room.EquipmentType
import com.example.apptemplates.data.room.Room
import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.data.user.User
import com.example.apptemplates.form.ScreenState
import com.example.apptemplates.form.UiError
import com.example.apptemplates.montserratFontFamily
import com.example.apptemplates.navigation.route.AppScreen
import com.example.apptemplates.presentation.login.sign_in.component.CommonLoadingSnackbar
import com.example.apptemplates.presentation.login.sign_in.component.getThemeTopAppBarColors
import com.example.apptemplates.presentation.main.home.ActiveReservations
import com.example.apptemplates.presentation.main.home.ActiveRooms
import com.example.apptemplates.presentation.main.home.HomeView
import com.example.apptemplates.presentation.main.home.HomeViewModel
import com.example.apptemplates.presentation.main.profile.ProfileView
import com.example.apptemplates.presentation.main.profile.ProfileViewModel
import com.example.apptemplates.presentation.main.profile.domain.SignOutUseCase
import com.example.apptemplates.presentation.main.reservation.ReservationView
import com.example.apptemplates.presentation.main.reservation.ReservationViewModel
import com.example.apptemplates.presentation.main.room_availability.RoomAvailabilityView
import com.example.apptemplates.presentation.main.room_availability.RoomAvailabilityViewModel
import com.example.apptemplates.presentation.main.settings.SettingsScreen
import com.example.apptemplates.presentation.main.settings.SettingsViewModel
import com.example.apptemplates.ui.theme.ThemeColors
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun AppScaffold(
    navController: NavHostController,
    onLogout: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val isRightDrawerOpen = remember { mutableStateOf(false) }
    val currentScreenState = remember { mutableStateOf(MainUiState()) }
    val theme = getThemeTopAppBarColors()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBarPreview(navController) { route ->
                    navController.navigate(route) {
                        popUpTo(AppScreen.Main.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            bottomBar = {
                BottomBar(navController, currentScreenState, onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(AppScreen.Main.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }, onOptionsClick = {
                    scope.launch {
                        isRightDrawerOpen.value = !isRightDrawerOpen.value
                    }
                })
            },
            snackbarHost = {

                if (currentScreenState.value.screenState == ScreenState.Loading) {
                    CommonLoadingSnackbar(
                        currentScreenState.value.screenState == ScreenState.Loading,
                        theme
                    )
                } else if (currentScreenState.value.screenState is ScreenState.Error) {
                    CommonErrorSnackBar(
                        currentScreenState.value.screenState is ScreenState.Error,
                        currentScreenState.value.screenState,
                        theme
                    ) {}
                }


            },
            content = { padding ->
                MainContentNavGraph(
                    navController,
                    padding,
                    onLogout,
                    onScreenStateChange = { state ->
                        currentScreenState.value = state
                    })
            })

        if (isRightDrawerOpen.value) {
            Box(modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable {
                    isRightDrawerOpen.value = false // Zamknij panel po kliknięciu w tło
                })
        }


        RightDrawerContent(
            navController = navController,
            isRightDrawerOpen = isRightDrawerOpen,
            modifier = Modifier.align(Alignment.TopEnd),
            onLogout = onLogout
        )

    }

}


data class MainUiState(
    //HOME STATES
    val user: User? = null,
    val screenState: ScreenState = ScreenState.Idle,
    val rooms: List<Room> = emptyList(),
    val reservations: List<Reservation> = emptyList(),
    val errors: List<UiError> = emptyList(),
    val lastUpdated: LocalDateTime? = null,  // Track when data was last updated
    val isLoading: Boolean = false,
    val reservationTimePeriods: TimePeriod = TimePeriod.TODAY,
    val filteredReservations: List<Reservation> = emptyList(),
    val reservationsLeft: Int? = null,
    val maxReservations: Int? = null,
    val lastReservationDate: String? = null,
    val showReservationDetailsDialog: Boolean = false,
    val selectedReservation: Reservation? = null,
    val selectedAdditionalEquipment: MutableList<EquipmentType> = mutableListOf(),

    // RESERVATION
    val reservationError: ReservationError? = null,
    val selectedDate: LocalDate? = null, // Data rozpoczecia i zakonczenia jezeli nie jest cykliczna
    val selectedTime: LocalTime? = null, // Godzina rozpoczecia
    val selectedEndTime: LocalTime? = null, // Godzina zakonczenia

    val selectedAttendees: Int = 1, // Liczba uczestnikow

    val isRecurring: Boolean = false, // Czy jest cykliczna
    val recurringFrequency: RecurrenceFrequency? = null, // Rodzaj cykliczonosci
    val endRecurrenceDate: LocalDate? = null,   // Zakonczenie cyklu

    val selectedFloor: Int? = null, // Wybrany piętro
    val floorName: String? = null,
    val selectedEquipment: List<EquipmentType> = emptyList(), // Wybrane conflictos

    val availableRooms: List<Room> = emptyList(),

    val selectedRoomToReserve: Room? = null,

    // PROFILE
    val username: String = ActiveUser.getUser()?.username ?: "Nieznajomy",
    val role: String = ActiveUser.getUser()?.role.toString() ?: "Nieznajomy",
    val email: String = ActiveUser.getUser()?.email ?: "nieznany",
    val isEmailVerified: Boolean = ActiveUser.isUserVerified(),
    val overallReservationCount: Int = ActiveReservations.getAllReservations().value.size,
    val lastSeen: Long = 1,

    //Availabilty
    val selectedFloorName: String = "Parter",
    val selectedRoomNumber: String = "",
    val selectedFloorNumber: Int? = null,
    val selectedDateCheck: LocalDate = LocalDate.now(),
    val selectedRoom: Room? = null,
    val times: List<Triple<Long, Long, Boolean>>? = null,
    val canSeeRoomAvailability: Boolean = false,


    // All
    val profileImageUrl: String = "",
)

enum class TimePeriod {
    TODAY, TOMORROW, WEEK, MONTH, ALL
}

sealed class ReservationError {
    data class TimeConflict(val message: String) : ReservationError()
    data class AttendeesConflict(val message: String) : ReservationError()
    data class RecurrenceConflict(val message: String) : ReservationError()
}

@Composable
fun BottomBar(
    navController: NavController,
    currentScreenState: MutableState<MainUiState>,
    onNavigate: (String) -> Unit,
    onOptionsClick: () -> Unit
) {
    val theme = getThemeTopAppBarColors()
    val isDarkTheme = isSystemInDarkTheme()

    val items = listOf(
        AppScreen.Main.Home,
        AppScreen.Main.RoomAvailability,
        AppScreen.Main.Reservation,
        AppScreen.Main.More
    )

    NavigationBar(
        modifier = Modifier
            .background(theme.gradientBrush)
            .padding(horizontal = 8.dp),
        containerColor = Color.Transparent,
        contentColor = theme.textColor,
        tonalElevation = 10.dp
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                icon = { NavigationBarItemIcon(screen, currentRoute) },
                label = { NavigationBarItemText(screen, currentRoute, theme) },
                selected = currentRoute == screen.route,
                onClick = {

                    if (currentScreenState.value.screenState == ScreenState.Loading) return@NavigationBarItem

                    if (currentRoute != screen.route) {

                        if (screen.route == AppScreen.Main.More.route) onOptionsClick()
                        else onNavigate(screen.route)

                    }

                }, colors = navigationBarItemColors(theme, isDarkTheme), alwaysShowLabel = true
            )
        }
    }
}

@Composable
private fun NavigationBarItemIcon(screen: AppScreen, currentRoute: String?) {
    Icon(
        screen.icon!!,
        contentDescription = screen.label!!,
        modifier = Modifier
            .size(24.dp)
            .scale(if (currentRoute == screen.route) 1.3f else 1f)
    )
}

@Composable
private fun NavigationBarItemText(screen: AppScreen, currentRoute: String?, theme: ThemeColors) {
    Text(
        text = screen.label!!,
        fontFamily = montserratFontFamily,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium,
        fontSize = if (currentRoute == screen.route) 12.sp else 10.sp,
        color = if (currentRoute == screen.route) theme.textColor else Color.Gray
    )
}

@Composable
private fun navigationBarItemColors(
    theme: ThemeColors,
    isDarkTheme: Boolean
): NavigationBarItemColors {
    return NavigationBarItemDefaults.colors(
        selectedIconColor = theme.textColor,
        unselectedIconColor = if (isDarkTheme) Color(0xFFD1D1D1) else Color(0xFF3C4043),
        selectedTextColor = theme.textColor,
        unselectedTextColor = if (isDarkTheme) Color(0xFFD1D1D1) else Color(0xFF3C4043),
        indicatorColor = Color.Transparent
    )
}


private fun NavGraphBuilder.homeScreen(
    padding: PaddingValues,
    onScreenStateChange: (MainUiState) -> Unit
) {
    composable(AppScreen.Main.Home.route) { backStackEntry ->
        val homeViewModel: HomeViewModel = viewModel(backStackEntry)

        val state by homeViewModel.state.collectAsState()
        onScreenStateChange(state)

        HomeView(
            padding = padding,
            viewModel = homeViewModel,
            uiState = state
        )
    }
}

private fun NavGraphBuilder.reservationScreen(
    padding: PaddingValues,
    onScreenStateChange: (MainUiState) -> Unit,
    navController: NavHostController
) {
    composable(AppScreen.Main.Reservation.route) { backStackEntry ->
        val reservationViewModel: ReservationViewModel = viewModel(backStackEntry)

        val state by reservationViewModel.state.collectAsState()
        onScreenStateChange(state)


        ReservationView(
            state = reservationViewModel.state.collectAsState().value,
            viewModel = reservationViewModel,
            padding = padding,
            modifier = Modifier.padding(padding),
            navigateOnSuccess = {
                navController.navigate(AppScreen.Main.Home.route) {
                    popUpTo(AppScreen.Main.route) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

private fun NavGraphBuilder.roomAvailabilityScreen(
    padding: PaddingValues,
    onScreenStateChange: (MainUiState) -> Unit
) {
    composable(AppScreen.Main.RoomAvailability.route) { backStackEntry ->
        val roomAvailabilityViewModel: RoomAvailabilityViewModel = viewModel(backStackEntry)

        val state by roomAvailabilityViewModel.state.collectAsState()
        onScreenStateChange(state)

        RoomAvailabilityView(
            state = roomAvailabilityViewModel.state.collectAsState().value,
            viewModel = roomAvailabilityViewModel,
            padding = padding,
            modifier = Modifier.padding(padding)
        )
    }
}

private fun NavGraphBuilder.profileScreen(
    padding: PaddingValues,
    onLogout: () -> Unit,
    onScreenStateChange: (MainUiState) -> Unit,
) {
    composable(AppScreen.Main.Profile.route) { backStackEntry ->
        val profileViewModel: ProfileViewModel = viewModel(backStackEntry)

        val state by profileViewModel.state.collectAsState()
        onScreenStateChange(state)


        ProfileView(
            state = profileViewModel.state.collectAsState().value,
            viewModel = profileViewModel,
            onLogout = {
                onLogout()
            },
            padding = padding
        )
    }
}

private fun NavGraphBuilder.settingsScreen() {
    composable(AppScreen.Main.Settings.route) { backStackEntry ->
        val settingsViewModel: SettingsViewModel = viewModel(backStackEntry)
        SettingsScreen(settingsViewModel)
    }
}

@Composable
private fun MainContentNavGraph(
    navController: NavHostController,
    padding: PaddingValues,
    onLogout: () -> Unit,
    onScreenStateChange: (MainUiState) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Main.Home.route,
    ) {

        homeScreen(padding, onScreenStateChange)
        reservationScreen(padding, onScreenStateChange, navController)
        roomAvailabilityScreen(padding, onScreenStateChange)
        profileScreen(padding, onLogout, onScreenStateChange)
        settingsScreen()
    }
}


@Composable
private fun RightDrawerContent(
    navController: NavController,
    isRightDrawerOpen: MutableState<Boolean>,
    modifier: Modifier,
    onLogout: () -> Unit
) {
    AnimatedVisibility(
        visible = isRightDrawerOpen.value, enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth }, // Panel wchodzi z prawej strony
            animationSpec = tween(durationMillis = 300)
        ), exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth }, // Panel wychodzi na prawo
            animationSpec = tween(durationMillis = 300)
        ), modifier = modifier // Ustawienie do prawej strony
    ) {
        Box(
            modifier = Modifier
                .width(250.dp)
                .fillMaxHeight()
                .background(Color.White) // Kolor panelu
        ) {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route

            CustomDrawerContent(
                activeScreen = currentRoute,
                onScreenSelected = { screen ->
                    isRightDrawerOpen.value = false
                    navController.navigate(screen) {
                        popUpTo(AppScreen.Main.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onLogout = {
                    onLogout()
                }
            )
        }
    }
}


@Composable
fun CustomDrawerContent(
    activeScreen: String?,
    onScreenSelected: (String) -> Unit,
    onLogout: () -> Unit
) {
    val topItems = listOf(
        AppScreen.Main.Home,
        AppScreen.Main.RoomAvailability,
        AppScreen.Main.Reservation,
        AppScreen.Main.Profile,
    )

    val bottomItems = listOf(
        AppScreen.Main.Settings, AppScreen.Main.LogOut
    )

    val backgroundGradient = if (isSystemInDarkTheme()) {
        listOf(Color(0xFF2E3A48), Color(0xFF1C252F))
    } else {
        listOf(Color(0xFFFDF6EC), Color(0xFFECE3D3)) // Warm light beige for a soft appearance
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = backgroundGradient))
    ) {
        // Header with Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo), // Change to your image
                contentDescription = "Header Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .shadow(10.dp, CircleShape)
                    .background(
                        Color(0xFFF5EEDC),
                        shape = CircleShape
                    ) // Soft beige background for the logo
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Menu items
        topItems.forEach { screen ->
            DrawerItem(
                icon = screen.icon!!,
                label = screen.label!!,
                isSelected = activeScreen == screen.route,
                onClick = { onScreenSelected(screen.route) }
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        HorizontalDivider(color = Color(0xFFDADADA).copy(alpha = 0.5f), thickness = 1.dp)

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        bottomItems.forEach { screen ->
            DrawerItem(
                icon = screen.icon!!,
                label = screen.label!!,
                isSelected = activeScreen == screen.route,
                onClick = {
                    if (screen.route == AppScreen.Main.LogOut.route) {
                        SignOutUseCase()()
                        ActiveReservations.setReservations(emptyList())
                        ActiveRooms.setRooms(emptyList())
                        ActiveUser.clearUser()
                        onLogout()
                    } else onScreenSelected(screen.route)
                }
            )
        }
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val backgroundColor =
        if (isSelected) Color(0xFFE0A800).copy(alpha = 0.3f) else Color.Transparent // Złoty odcień dla zaznaczonego tła
    val textColor =
        if (isSelected) Color(0xFF3D1F1F) else Color(0xFF5D4037) // Ciemny brąz dla zaznaczonego tekstu




    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = textColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            color = textColor,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
    }
}



