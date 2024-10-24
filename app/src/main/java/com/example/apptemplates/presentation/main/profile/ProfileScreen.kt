package com.example.apptemplates.presentation.main.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptemplates.BottomBarPreview
import com.example.apptemplates.R
import com.example.apptemplates.TopBarPreview
import com.example.apptemplates.ui.theme.AppTextStyles

@Preview(showBackground = true)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = ProfileViewModel(),
) {

    val state by viewModel.state.collectAsState()


    Scaffold(
        topBar = { TopBarPreview() },
        bottomBar = { BottomBarPreview() },
        content = { padding -> ProfileView(state, viewModel, padding) }
    )

}

@Composable
fun ProfileView(state: ProfileState, viewModel: ProfileViewModel, padding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFFFAFAFA),
                        Color(0xFFE0F7FA)
                    )
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Profile Picture Placeholder
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.White, shape = CircleShape)
                    .clip(CircleShape)
                    .border(4.dp, Color.LightGray, CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with real image
                    contentDescription = "Profile Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User's Name and Role
            Text(
                text = state.username,
                style = AppTextStyles.headerStyle(Color(0xFF1B5E20)),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Role: ${state.role}",
                style = AppTextStyles.subTextStyle(Color.Gray),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Reservation Statistics
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                StatItem(stat = state.overallReservationCount.toString(), label = "Reservations")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Email Information with Stylish Design
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFBDBDBD), RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "Email Address:",
                        style = AppTextStyles.subTextStyle(Color(0xFF757575)).copy(fontSize = 20.sp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Icon",
                            tint = Color(0xFF0288D1),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = state.email,
                            style = AppTextStyles.topBarSubtitleStyle(Color.Black).copy(
                                fontSize = 18.sp // Adjusted for better readability
                            ),
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Email Verification Status
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(
                            imageVector = if (state.isEmailVerified) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                            contentDescription = "Email Verification Status",
                            tint = if (state.isEmailVerified) Color(0xFF4CAF50) else Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (state.isEmailVerified) "Email Verified" else "Email Not Verified",
                            style = AppTextStyles.topBarSubtitleStyle(
                                if (state.isEmailVerified) Color(
                                    0xFF43A047
                                ) else Color.Red
                            ).copy(
                                fontSize = 18.sp // Adjusted for better readability
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons for Edit Profile and Logout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Edit Profile Button
                Button(
                    onClick = { /* Open Edit Profile Screen */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0277BD))
                ) {
                    Text(
                        text = "Edit Profile",
                        style = AppTextStyles.buttonStyle(Color.White)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Logout Button
                Button(
                    onClick = { /* Handle Logout Action */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFD32F2F))
                ) {
                    Text(
                        text = "Logout",
                        style = AppTextStyles.buttonStyle(Color.White)
                    )
                }
            }
        }
    }
}


@Composable
fun StatItem(stat: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stat,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )
    }
}

fun formatLastSeen(lastSeen: Long): String {
    // Here, you can convert the last seen timestamp into a human-readable format
    return "5 minutes ago" // Placeholder
}