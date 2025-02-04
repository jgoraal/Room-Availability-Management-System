package com.example.apptemplates.presentation.screens.home.profile

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.apptemplates.R
import com.example.apptemplates.data.model.model.user.Role
import com.example.apptemplates.domain.usecase.ActiveUser
import com.example.apptemplates.presentation.screens.home.base.MainUiState
import com.example.apptemplates.utils.theme.getContentBackGround
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@Composable
fun ProfileView(
    state: MainUiState,
    viewModel: ProfileViewModel,
    onLogout: () -> Unit,
    padding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(getContentBackGround())
            .padding(padding)
    ) {

        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                ProfileHeaderWithStatistics(state, viewModel, onLogout)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

    }
}


@Composable
fun ProfileHeaderWithStatistics(
    state: MainUiState,
    viewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val gradientColors = if (isDarkTheme) {
        listOf(
            Color(0xFF004D40),
            Color(0xFF2E7D6A)
        )
    } else {
        listOf(
            Color(0xFFFFF9E6),
            Color(0xFFFFE7B8)
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(colors = gradientColors)
            )
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {
        ProfileImage(state, viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        ProfileTextSection(state)
        Spacer(modifier = Modifier.height(16.dp))
        ReservationStatisticsSection(state)
        Spacer(modifier = Modifier.height(16.dp))
        EmailInformationCard(state)
        Spacer(modifier = Modifier.height(32.dp))
        ProfileActions(viewModel, onLogout)
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileImage(state: MainUiState, viewModel: ProfileViewModel) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            android.Manifest.permission.READ_MEDIA_IMAGES
        else
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val isDarkTheme = isSystemInDarkTheme()


    val backgroundColor =
        if (isDarkTheme) Color(0xFF1B3A3A) else Color(0xFFE0F2E9)
    val borderColor =
        if (isDarkTheme) Color(0xFF26A69A) else Color(0xFF8FA58D)
    val shadowColor =
        if (isDarkTheme) Color(0xFF004D40).copy(alpha = 0.6f) else Color.LightGray

    var imageUri by remember { mutableStateOf<Uri?>(null) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it

            viewModel.uploadProfileImage(it)
        }
    }

    Box(
        modifier = Modifier
            .size(100.dp)
            .background(backgroundColor, shape = CircleShape)
            .clip(CircleShape)
            .border(6.dp, borderColor, CircleShape)
            .shadow(10.dp, CircleShape, ambientColor = shadowColor)
            .clickable {
                if (permissionState.status.isGranted) {
                    launcher.launch("image/*")
                } else {
                    permissionState.launchPermissionRequest()
                }
            }
    ) {
        if (state.profileImageUrl.isNotEmpty()) {

            AsyncImage(
                model = state.profileImageUrl,
                contentDescription = "Profile Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {

            Image(
                painter = painterResource(id = R.drawable.guest_profile_icon),
                contentDescription = "Profile Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@Composable
fun ProfileTextSection(state: MainUiState) {
    val isDarkTheme = isSystemInDarkTheme()

    val usernameColor =
        if (isDarkTheme) Color(0xFFA8D5BA) else Color(0xFF2E5234)
    val roleBackgroundColor =
        if (isDarkTheme) Color(0xFF004D40) else Color(0xFFB3C2AD)
    val roleTextColor =
        if (isDarkTheme) Color(0xFFE0F7FA) else Color(0xFF2E5234)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = state.username,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = usernameColor,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .background(
                    color = roleBackgroundColor,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Text(
                text = "Rola: ${ActiveUser.getUser()?.role.getFormattedRole()}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = roleTextColor,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun ReservationStatisticsSection(state: MainUiState) {
    val isDarkTheme = isSystemInDarkTheme()

    val gradientColors = if (isDarkTheme) {
        listOf(
            Color(0xFF004D40),
            Color(0xFF2E7D6A)
        )
    } else {
        listOf(
            Color(0xFFEFF4E9),
            Color(0xFFD4DEC8)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            StatItem(
                stat = state.overallReservationCount.toString(),
                label = state.overallReservationCount.getFormattedReservationCount()
            )

        }
    }
}


@Composable
fun StatItem(stat: String, label: String) {
    val isDarkTheme = isSystemInDarkTheme()

    val backgroundColor =
        if (isDarkTheme) Color(0xFF2E3B32) else Color(0xFFF4F7EC)
    val statTextColor =
        if (isDarkTheme) Color(0xFFA8D5BA) else Color(0xFF2E5234)
    val labelTextColor =
        if (isDarkTheme) Color(0xFFB2C9BD) else Color(0xFF6E8B6D)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = stat,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = statTextColor,
                letterSpacing = 1.1.sp
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = labelTextColor,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun EmailInformationCard(state: MainUiState) {
    val isDarkTheme = isSystemInDarkTheme()

    val cardColor =
        if (isDarkTheme) Color(0xFF1B3A32) else Color(0xFFF0F6EB)
    val headerColor =
        if (isDarkTheme) Color(0xFFA8D5BA) else Color(0xFF7A9478)
    val iconColor =
        if (isDarkTheme) Color(0xFF80CBC4) else Color(0xFF4B6753)
    val emailTextColor =
        if (isDarkTheme) Color(0xFFB2C9BD) else Color(0xFF3A4A37)
    val verifiedColor =
        if (isDarkTheme) Color(0xFF66BB6A) else Color(0xFF66BB6A)
    val notVerifiedColor =
        if (isDarkTheme) Color(0xFFE57373) else Color(0xFFE57373)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Adres email:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = headerColor,
                    fontWeight = FontWeight.SemiBold
                )
            )


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = state.email,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = emailTextColor,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .wrapContentWidth()
                        .horizontalScroll(rememberScrollState()),
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }

            // Weryfikacja emaila
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = if (state.isEmailVerified) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = if (state.isEmailVerified) verifiedColor else notVerifiedColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (state.isEmailVerified) "Email zweryfikowany" else "Email niezweryfikowany",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (state.isEmailVerified) verifiedColor else notVerifiedColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}


@Composable
fun ProfileActions(viewModel: ProfileViewModel, onLogout: () -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()

    val deleteButtonColor =
        if (isDarkTheme) Color(0xFF8B0000) else Color(0xFFFF5252)
    val logoutButtonColor =
        if (isDarkTheme) Color(0xFF00574B) else Color(0xFF4CAF50)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        ProfileButton(
            text = "Usuń konto",
            color = deleteButtonColor,
            onClick = { viewModel.deleteUserAccount(onLogout) }
        )
        Spacer(modifier = Modifier.width(24.dp))
        ProfileButton(
            text = "Wyloguj",
            color = logoutButtonColor,
            onClick = { viewModel.logout(onLogout) }
        )
    }
}

@Composable
fun ProfileButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(50.dp)
            .width(150.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.1.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

private fun Int.getFormattedReservationCount(): String {
    return when {
        this == 0 -> "Rezerwacji"
        this == 1 -> "Rezerwacja"
        this % 10 in 2..4 && this % 100 !in 12..14 -> "Rezerwacje"
        else -> "Rezerwacji"
    }
}


private fun Role?.getFormattedRole(): String {
    return when (this) {
        Role.ADMIN -> "Administrator"
        Role.EMPLOYEE -> "Pracownik"
        Role.STUDENT -> "Student"
        else -> "Gość"
    }
}
