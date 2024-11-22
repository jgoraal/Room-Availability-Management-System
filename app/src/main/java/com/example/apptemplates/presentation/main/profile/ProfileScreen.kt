package com.example.apptemplates.presentation.main.profile

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.apptemplates.R
import com.example.apptemplates.data.user.ActiveUser
import com.example.apptemplates.presentation.main.temp.MainUiState
import com.example.apptemplates.ui.theme.getContentBackGround
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getContentBackGround())
            .padding(padding)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ProfileHeaderWithStatistics(state, viewModel, onLogout)
            Spacer(modifier = Modifier.height(24.dp))
            /*//ReservationStatistics(state)
            //Spacer(modifier = Modifier.height(24.dp))
            EmailInformationCard(state)
            Spacer(modifier = Modifier.height(32.dp))
            ProfileActions()*/
        }
    }
}


@Composable
fun ProfileHeaderWithStatistics(
    state: MainUiState,
    viewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF7FBEF), // Jasny oliwkowy u góry
                        Color(0xFFE0EAD6)  // Subtelny oliwkowy u dołu
                    )
                )
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

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher do wyboru obrazu
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            // Prześlij obraz do Firebase Storage
            viewModel.uploadProfileImage(it)
        }
    }

    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color(0xFFE0F2E9), shape = CircleShape)
            .clip(CircleShape)
            .border(6.dp, Color(0xFF8FA58D), CircleShape)
            .shadow(10.dp, CircleShape)
            .clickable {
                if (permissionState.status.isGranted) {
                    launcher.launch("image/*")
                } else {
                    permissionState.launchPermissionRequest()
                }
            }
    ) {
        if (state.profileImageUrl.isNotEmpty()) {
            // Wyświetl obraz z URL za pomocą Coil
            AsyncImage(
                model = state.profileImageUrl,
                contentDescription = "Profile Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // Domyślny obraz
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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = state.username,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color(0xFF2E5234),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            ),
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .background(
                    Color(0xFFB3C2AD),
                    shape = RoundedCornerShape(50)
                )
                .padding(vertical = 4.dp, horizontal = 12.dp)
        ) {
            Text(
                text = "Rola: ${ActiveUser.getUser()?.role ?: "Gość"}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ReservationStatisticsSection(state: MainUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            /*.background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFEFF4E9), // Jasny oliwkowy
                        Color(0xFFD4DEC8)  // Subtelny, chłodny oliwkowy
                    )
                )
            )*/
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            StatItem(stat = state.overallReservationCount.toString(), label = "Rezerwacje")
            // Dodaj dodatkowe statystyki, jeśli potrzebujesz
        }
    }
}

@Composable
fun StatItem(stat: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF4F7EC)) // Jasne tło dla wyróżnienia statystyki
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = stat,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E5234),
                letterSpacing = 1.1.sp
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF6E8B6D),
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun EmailInformationCard(state: MainUiState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F6EB)), // Subtelny, jasny zielony
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Nagłówek "Adres email"
            Text(
                text = "Adres email:",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF7A9478), // Stonowany zielony dla harmonii z tłem
                    fontWeight = FontWeight.SemiBold
                )
            )

            // Adres email z ikoną
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = Color(0xFF4B6753), // Głęboka zieleń dla kontrastu
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = state.email,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF3A4A37), // Głęboka oliwkowa zieleń
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1
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
                    tint = if (state.isEmailVerified) Color(0xFF66BB6A) else Color(0xFFE57373), // Zieleń dla zweryfikowanego, czerwony dla nieweryfikowanego
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (state.isEmailVerified) "Email zweryfikowany" else "Email niezweryfikowany",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (state.isEmailVerified) Color(0xFF66BB6A) else Color(0xFFE57373), // Dopasowanie koloru do ikony
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}


@Composable
fun ProfileActions(viewModel: ProfileViewModel, onLogout: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ProfileButton(
            text = "Usuń konto",
            color = Color(0xFF4A6A49), // Głęboki, elegancki zielony
            onClick = { viewModel.deleteUserAccount(onLogout) }
        )
        Spacer(modifier = Modifier.width(16.dp))
        ProfileButton(
            text = "Wyloguj",
            color = Color(0xFF9E9D24), // Ciepły, ciemniejszy zielony
            onClick = { viewModel.logout(onLogout) }
        )
    }
}


@Composable
fun ProfileButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(50.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}
