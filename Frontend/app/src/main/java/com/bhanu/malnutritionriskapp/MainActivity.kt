package com.bhanu.malnutritionriskapp

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.Context
import java.io.File
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import androidx.compose.material3.RadioButton
import com.itextpdf.layout.Document
import androidx.compose.material3.AlertDialog
import android.os.Environment
import androidx.compose.material3.TextButton
import com.itextpdf.layout.element.Paragraph
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.IconButton
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.ui.window.Dialog
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bhanu.malnutritionriskapp.ui.theme.AppBackground
import com.bhanu.malnutritionriskapp.ui.theme.CardBackground
import com.bhanu.malnutritionriskapp.ui.theme.MalnutritionRiskAppTheme
import com.bhanu.malnutritionriskapp.ui.theme.PrimaryBlue
import com.bhanu.malnutritionriskapp.ui.theme.PrimaryBlueDark
import com.bhanu.malnutritionriskapp.ui.theme.RiskHigh
import com.bhanu.malnutritionriskapp.ui.theme.RiskHighBg
import com.bhanu.malnutritionriskapp.ui.theme.RiskLow
import com.bhanu.malnutritionriskapp.ui.theme.RiskLowBg
import com.bhanu.malnutritionriskapp.ui.theme.RiskMedium
import com.bhanu.malnutritionriskapp.ui.theme.RiskMediumBg
import com.bhanu.malnutritionriskapp.ui.theme.TextPrimary
import com.bhanu.malnutritionriskapp.ui.theme.TextSecondary
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

enum class AppScreen {
    LOGIN,
    HOME,
    PREDICT,
    RESULT,
    RECORDS,
    RECORD_DETAILS,
    DIET,
    NEARBY,
    PROFILE
}

fun normalizeRisk(rawRisk: String): String {
    val r = rawRisk.lowercase()

    return when {
        r.contains("high") -> "High"
        r.contains("medium") || r.contains("moderate") -> "Medium"
        r.contains("low") -> "Low"
        r.contains("normal") || r.contains("healthy") -> "Normal"
        else -> "Unknown"
    }
}

data class PredictionRecord(
    val childName: String,
    val childId: String,
    val place: String,
    val dateTime: String,

    val age: Int,
    val weight: Double,
    val height: Int,
    val muac: Double,
    val hemoglobin: Double,

    val sex: String,
    val motherEdu: String,
    val waterSource: String,
    val toilet: String,
    val wealthIndex: String,
    val householdsize: String,
    val birthweight: String,

    val risk: String
)

// -------------------- MAIN ACTIVITY --------------------

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MalnutritionRiskAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

val defaultWorker = HealthWorker(
    id = "HW1023",
    name = "Ramesh Kumar",
    role = "ASHA Worker",
    village = "Nagandenahalli, Karnataka"
)
// -------------------- SCREEN CONTROLLER --------------------
@Composable
fun MainScreen() {
    var dietMessage by remember { mutableStateOf("") }

    var currentScreen by remember { mutableStateOf(AppScreen.LOGIN) }
    var selectedRecord by remember { mutableStateOf<PredictionRecord?>(null) }
    var finalRisk by remember { mutableStateOf("") }
    var childId by remember { mutableStateOf("") }
    var householdSize by remember { mutableStateOf("") }
    var birthWeight by remember { mutableStateOf("") }
    val records = remember {
        mutableStateListOf(

            PredictionRecord(
                childName = "Ravi",
                childId = "1234-5678",
                place = "Village A",
                dateTime = "12 Jan 2026, 10:30 AM",

                age = 24,
                weight = 9.2,
                height = 78,
                muac = 11.0,
                hemoglobin = 8.5,

                sex = "Male",
                motherEdu = "No education",
                waterSource = "Unsafe",
                toilet = "Open",
                wealthIndex = "Poor",
                householdsize = "5",
                birthweight = "2.3",

                risk = "High"
            ),

            PredictionRecord(
                childName = "Sita",
                childId = "2345-6789",
                place = "Village B",
                dateTime = "12 Jan 2026, 11:15 AM",

                age = 30,
                weight = 10.1,
                height = 82,
                muac = 11.5,
                hemoglobin = 9.0,

                sex = "Female",
                motherEdu = "Primary",
                waterSource = "Protected well",
                toilet = "Shared",
                wealthIndex = "Medium",
                householdsize = "4",
                birthweight = "2.5",

                risk = "High"
            ),

            PredictionRecord(
                childName = "Aman",
                childId = "3456-7890",
                place = "Village C",
                dateTime = "12 Jan 2026, 01:00 PM",

                age = 36,
                weight = 12.0,
                height = 90,
                muac = 12.8,
                hemoglobin = 10.5,

                sex = "Male",
                motherEdu = "Primary",
                waterSource = "Tap",
                toilet = "Private",
                wealthIndex = "Medium",
                householdsize = "6",
                birthweight = "2.8",

                risk = "Medium"
            ),

            PredictionRecord(
                childName = "Kiran",
                childId = "4567-8901",
                place = "Village D",
                dateTime = "12 Jan 2026, 02:20 PM",

                age = 40,
                weight = 13.5,
                height = 95,
                muac = 13.6,
                hemoglobin = 11.8,

                sex = "Male",
                motherEdu = "Higher",
                waterSource = "Tap",
                toilet = "Private",
                wealthIndex = "Rich",
                householdsize = "4",
                birthweight = "3.0",

                risk = "Low"
            ),

            PredictionRecord(
                childName = "Meena",
                childId = "5678-9012",
                place = "Village E",
                dateTime = "12 Jan 2026, 03:45 PM",

                age = 42,
                weight = 14.8,
                height = 100,
                muac = 14.0,
                hemoglobin = 12.5,

                sex = "Female",
                motherEdu = "Higher",
                waterSource = "Tap",
                toilet = "Private",
                wealthIndex = "Rich",
                householdsize = "3",
                birthweight = "3.2",

                risk = "Normal"
            )
        )
    }
    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = ""
    ) { screen ->

        when (screen) {

            AppScreen.LOGIN -> LoginScreen(
                onLoginSuccess = { currentScreen = AppScreen.HOME },
                onGuestLogin = { currentScreen = AppScreen.HOME }
            )

            AppScreen.HOME -> HomeScreen(
                onPredict = { currentScreen = AppScreen.PREDICT },
                onRecords = { currentScreen = AppScreen.RECORDS },
                onCharts = { currentScreen = AppScreen.DIET },
                onNearby = { currentScreen = AppScreen.NEARBY },
                onProfile = { currentScreen = AppScreen.PROFILE },
                onLogout = { currentScreen = AppScreen.LOGIN }
            )

            AppScreen.PREDICT -> InputScreen(
                records = records,   // ✅ ADD THIS
                onResult = { childName, childId, place, request, response ->

                    // ✅ 1. Add record here
                    val currentDateTime = SimpleDateFormat(
                        "dd MMM yyyy, hh:mm a",
                        Locale.getDefault()
                    ).format(Date())

                    records.add(
                        PredictionRecord(
                            childName = childName,
                            childId = childId,
                            place = place,
                            dateTime = currentDateTime,

                            age = request.age,
                            weight = request.weight,
                            height = request.height,
                            muac = request.muac,
                            hemoglobin = request.hemoglobin,

                            sex = request.sex,
                            motherEdu = request.mother_edu,
                            waterSource = request.water_source,
                            toilet = request.toilet,
                            wealthIndex = request.wealth_index.toString(),
                            householdsize = request.household_size.toString(),
                            birthweight = request.birth_weight.toString(),

                            risk = response.risk
                        )
                    )

                    // ✅ 2. Then update result
                    finalRisk = response.risk
                    dietMessage = response.diet_message

                    // ✅ 3. Then navigate
                    currentScreen = AppScreen.RESULT
                },
                onBack = { currentScreen = AppScreen.HOME }
            )

            AppScreen.RESULT -> ResultScreen(
                risk = finalRisk,
                dietMessage = dietMessage,
                onBack = { currentScreen = AppScreen.HOME },
                onEdit = { currentScreen = AppScreen.PREDICT }   // ✅ ADD THIS
            )

            AppScreen.RECORDS -> RecordsScreen(
                records = records,
                onBack = { currentScreen = AppScreen.HOME },
                onRecordClick = { record ->
                    selectedRecord = record
                    currentScreen = AppScreen.RECORD_DETAILS
                }
            )

            AppScreen.RECORD_DETAILS -> {
                selectedRecord?.let {
                    RecordDetailsScreen(
                        record = it,
                        onBack = { currentScreen = AppScreen.RECORDS }
                    )
                }
            }

            AppScreen.DIET -> DietScreen(
                onBack = { currentScreen = AppScreen.HOME }
            )

            AppScreen.NEARBY -> NearbyScreen {
                currentScreen = AppScreen.HOME
            }

            AppScreen.PROFILE -> ProfileScreen(
                worker = defaultWorker,
                totalRecords = records.size,
                onBack = { currentScreen = AppScreen.HOME }
            )
        }
    }
}
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onGuestLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        PrimaryBlue,
                        AppBackground
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {

            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // App Title
                Text(
                    text = "PoshanRisk",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlueDark
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Subtitle
                Text(
                    text = "Child Malnutrition Risk Status Prediction",
                    fontSize = 14.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Username
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Health Worker ID") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(22.dp))

                // Login Button
                Button(
                    onClick = {
                        if (username == "Testlogin" && password == "9999") {
                            error = ""
                            onLoginSuccess()
                        } else {
                            error = "Invalid username or password"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text(
                        text = "Login",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Error Message
                if (error.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = error,
                        color = RiskHigh,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Guest Login
                TextButton(onClick = onGuestLogin) {
                    Text(
                        text = "Continue as Guest",
                        color = PrimaryBlue
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    onPredict: () -> Unit,
    onRecords: () -> Unit,
    onCharts: () -> Unit,
    onNearby: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
    ) {

        // 🔹 MODERN HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            PrimaryBlue,
                            PrimaryBlueDark
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Malnutrition Risk App",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Smart health screening & early intervention",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 FEATURE GRID
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                HomeFeatureCard(
                    title = "Predict Risk",
                    icon = "📈",
                    onClick = onPredict
                )
                HomeFeatureCard(
                    title = "Records",
                    icon = "📋",
                    onClick = onRecords
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                HomeFeatureCard(
                    title = "Charts & Diet",
                    icon = "🥗",
                    onClick = onCharts
                )
                HomeFeatureCard(
                    title = "Nearby",
                    icon = "📍",
                    onClick = onNearby
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 🔹 BOTTOM ACTIONS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedButton(
                onClick = onProfile,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = PrimaryBlue
                )
            ) {
                Text("👤 Account")
            }

            Button(
                onClick = onLogout,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                )
            ) {
                Text("Back to Login")
            }
        }
    }
}

@Composable
fun RowScope.HomeFeatureCard(
    title: String,
    icon: String,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(500),
        label = ""
    )

    Card(
        modifier = Modifier
            .weight(1f)
            .height(130.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = icon,
                fontSize = 34.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun HomeButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    ) {
        Text(text, fontSize = 18.sp)
    }
}
@Composable
fun PlaceholderScreen(
    title: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(title, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(30.dp))

        Text("This screen will be implemented next.")

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    records: List<PredictionRecord>,
    onBack: () -> Unit,
    onRecordClick: (PredictionRecord) -> Unit
){
    var selectedFilter by remember { mutableStateOf("All") }
    var showChart by remember { mutableStateOf(true) }
    val filteredRecords = when (selectedFilter) {
        "High" -> records.filter { normalizeRisk(it.risk) == "High" }
        "Medium" -> records.filter { normalizeRisk(it.risk) == "Medium" }
        "Low" -> records.filter { normalizeRisk(it.risk) == "Low" }
        "Normal" -> records.filter { normalizeRisk(it.risk) == "Normal" }
        else -> records
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Prediction Records",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        // ✅ FILTER CHIPS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("All", "High", "Medium", "Low").forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ RECORD LIST
        if (filteredRecords.isEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("No records found.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredRecords) { record ->
                RecordCard(
                        record = record,
                        onRecordClick = onRecordClick
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF5F5F5)
            )
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Statistics",
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = if (showChart) "˅" else "˄",
                        fontSize = 20.sp,
                        modifier = Modifier.clickable {
                            showChart = !showChart
                        }
                    )
                }
                // count risks
                val high = records.count { it.risk == "High" }
                val medium = records.count { it.risk == "Medium" }
                val low = records.count { it.risk == "Low" }
                val normal = records.count { it.risk == "Normal" }
                if (showChart) {
                    RiskPieChart(records)

                    Spacer(modifier = Modifier.height(12.dp))

                    LegendRow(Color.Red, "High : $high")
                    LegendRow(Color(0xFFFF9800), "Medium : $medium")
                    LegendRow(Color(0xFFFFC107), "Low : $low")
                    LegendRow(Color.Green, "Normal : $normal")
                }
            }
        }


        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
fun RecordDetailsScreen(
    record: PredictionRecord,
    onBack: () -> Unit
) {
    var savedFileUri by remember { mutableStateOf<Uri?>(null) }
    val records = remember { mutableStateListOf<PredictionRecord>() }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Child Record Details",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Divider()

// -------- BASIC INFO ----------
        Text("Name: ${record.childName}")
        if (record.childId.isNotBlank()) {
            Text("Aadhaar / ABHA ID: ${record.childId}")
        }
        Text("Place: ${record.place}")
        Text("Date: ${record.dateTime}")

        Spacer(Modifier.height(12.dp))


// -------- HEALTH INPUTS ----------
        Text("Health Details", fontWeight = FontWeight.Bold)

        Text("Age: ${record.age} months")
        Text("Weight: ${record.weight} kg")
        Text("Height: ${record.height} cm")
        Text("MUAC: ${record.muac} cm")
        Text("Hemoglobin: ${record.hemoglobin} g/dL")

        Spacer(Modifier.height(12.dp))

// -------- SOCIO ECONOMIC ----------
        Text("Socio-economic Details", fontWeight = FontWeight.Bold)

        // -------- SOCIO ECONOMIC ----------
        Text("Sex: ${record.sex}")
        Text("Mother Education: ${record.motherEdu}")
        Text("Wealth Index: ${record.wealthIndex}")
        Text("Household Size: ${record.householdsize}")
        Text("Birth Weight: ${record.birthweight} kg")
        Text("Water Source: ${record.waterSource}")
        Text("Toilet Facility: ${record.toilet}")

        Spacer(Modifier.height(12.dp))

// -------- RESULT ----------
        Text("Prediction Result", fontWeight = FontWeight.Bold)

        Text("Risk Level: ${record.risk}")

        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    savedFileUri = exportFullReport(context, record)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Export")
            }
            Button(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }
            if (savedFileUri != null) {
                AlertDialog(
                    onDismissRequest = { savedFileUri = null },
                    title = { Text("File Saved") },
                    text = { Text("PDF saved successfully in Downloads folder.") },
                    confirmButton = {
                        TextButton(onClick = {
                            openPdf(context, savedFileUri!!)
                            savedFileUri = null
                        }) {
                            Text("View")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { savedFileUri = null }) {
                            Text("Close")
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun DietScreen(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Diet & Nutrition Guidance",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        DietSection(
            title = "Normal",
            color = Color.Green,
            imageRes = R.drawable.diet_normal,
            points = listOf(
                "Balanced diet with cereals, pulses, vegetables",
                "Milk or dairy once daily",
                "Seasonal fruits",
                "Regular growth monitoring"
            )
        )

        DietSection(
            title = "Low Risk",
            color = Color(0xFFFFC107),
            imageRes = R.drawable.diet_low,
            points = listOf(
                "Fruits and vegetables daily",
                "Milk and curd",
                "One meal per day",
                "Balanced home food"
            )
        )

        DietSection(
            title = "Medium Risk",
            color = Color(0xFFFF9800),
            imageRes = R.drawable.diet_medium,
            points = listOf(
                "Eggs, pulses, groundnuts",
                "Milk twice daily",
                "High-protein foods"
            )
        )
        DietSection(
            title = "High Risk",
            color = Color.Red,
            imageRes = R.drawable.diet_high,
            points = listOf(
                "Energy-dense foods",
                "Small frequent meals",
                "Medical supervision required"
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Composable
fun DietSection(
    title: String,
    color: Color,
    imageRes: Int,
    points: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = color,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "$title diet foods",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            points.forEach {
                Text("• $it")
            }
        }
    }
}


@Composable
fun RecordCard(
    record: PredictionRecord,
    onRecordClick: (PredictionRecord) -> Unit
) {
    val color = when (record.risk) {
        "High" -> Color.Red
        "Medium" -> Color(0xFFFF9800)
        "Low" -> Color(0xFFFFC107)
        else -> Color.Green
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRecordClick(record) },   // ✅ FIXED
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(record.childName, fontWeight = FontWeight.Bold)
                Text("Risk: ${record.risk}", color = color)
                Text(record.dateTime, fontSize = 12.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = ">",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}

fun validateInputs(
    age: String,
    weight: String,
    height: String,
    muac: String,
    hemoglobin: String,
    householdSize: String,
    birthWeight: String
): String? {

    val ageValue = age.toIntOrNull() ?: return "Invalid age"
    val weightValue = weight.toDoubleOrNull() ?: return "Invalid weight"
    val heightValue = height.toIntOrNull() ?: return "Invalid height"
    val muacValue = muac.toDoubleOrNull() ?: return "Invalid MUAC"
    val hbValue = hemoglobin.toDoubleOrNull() ?: return "Invalid hemoglobin"
    val householdValue = householdSize.toIntOrNull() ?: return "Invalid household size"
    val birthWeightValue = birthWeight.toDoubleOrNull() ?: return "Invalid birth weight"

    if (ageValue !in 0..60) return "Age must be 0–60 months"
    if (weightValue !in 2.0..25.0) return "Weight must be 2–25 kg"
    if (heightValue !in 40..120) return "Height must be 40–120 cm"
    if (muacValue !in 8.0..20.0) return "MUAC must be 8–20 cm"
    if (hbValue !in 5.0..18.0) return "Hemoglobin must be 5–18 g/dL"
    if (householdValue !in 1..15) return "Household size must be 1–15"
    if (birthWeightValue !in 1500.0..5000.0) return "Birth weight must be 1500–5000 g"

    return null
}

// -------------------- INPUT SCREEN --------------------

@Composable
fun InputScreen(
    records: MutableList<PredictionRecord>,
    onResult: (
        String,
        String,// childName
        String,      // place
        PatientRequest,
        PredictionResponse
    ) -> Unit,
    onBack: () -> Unit
) {
    var childName by remember { mutableStateOf("") }
    var childId by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var scannedImageUri by remember { mutableStateOf<Uri?>(null) }

    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var muac by remember { mutableStateOf("") }
    var hemoglobin by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var motherEdu by remember { mutableStateOf("") }
    var waterSource by remember { mutableStateOf("") }
    var toilet by remember { mutableStateOf("") }
    var wealthIndex by remember { mutableStateOf("") }
    var householdSize by remember { mutableStateOf("") }
    var birthWeight by remember { mutableStateOf("") }
    var isAutoFilled by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var ageError by remember { mutableStateOf("") }
    var weightError by remember { mutableStateOf("") }
    var heightError by remember { mutableStateOf("") }
    var muacError by remember { mutableStateOf("") }
    var hbError by remember { mutableStateOf("") }
    var generalError by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var openDateDialog by remember { mutableStateOf(false) }

    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { bitmap: Bitmap? ->

            if (bitmap != null) {

                val image = InputImage.fromBitmap(bitmap, 0)
                val recognizer =
                    TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        val ocrText = visionText.text
                        // ---------- CLEAN OCR LINES (IGNORE REGIONAL LANGUAGES) ----------
                        val rawLines = ocrText.split("\n")

                        val lines = rawLines
                            .map { it.trim() }
                            .filter { it.isNotBlank() }
                            .filter {
                                // keep only lines with English letters or numbers
                                it.any { ch -> ch in 'A'..'Z' || ch in 'a'..'z' || ch.isDigit() }
                            }
                        var detectedName: String? = null
                        var detectedSex: String? = null
                        var detectedAadhaar: String? = null

                        // ---------- DOB DETECTION (MULTI-LANGUAGE SAFE) ----------
                        var detectedDob: String? = null

                        for (i in lines.indices) {
                            val line = lines[i].lowercase()

                            // Case 1: Full date (DD-MM-YYYY / DD/MM/YYYY)
                            val fullDate = Regex("""\b\d{2}[-/]\d{2}[-/]\d{4}\b""")
                            if (detectedDob == null && fullDate.containsMatchIn(line)) {
                                detectedDob = fullDate.find(line)?.value?.replace("/", "-")
                                break
                            }

                            // Case 2: DOB / Birth keyword + year
                            if (
                                detectedDob == null &&
                                (line.contains("dob") || line.contains("birth"))
                            ) {
                                val year = Regex("""\b(19|20)\d{2}\b""").find(line)?.value
                                if (year != null) {
                                    detectedDob = "01-01-$year"
                                    break
                                }

                                // OCR split case → check next line
                                if (i + 1 < lines.size) {
                                    val nextLine = lines[i + 1]
                                    val nextYear = Regex("""\b(19|20)\d{2}\b""").find(nextLine)?.value
                                    if (nextYear != null) {
                                        detectedDob = "01-01-$nextYear"
                                        break
                                    }
                                }
                            }
                        }

// Case 3: LAST fallback — standalone year (English-only now)
                        if (detectedDob == null) {
                            for (line in lines) {
                                val year = Regex("""\b(19|20)\d{2}\b""").find(line)?.value
                                if (year != null) {
                                    detectedDob = "01-01-$year"
                                    break
                                }
                            }
                        }
// ---------- SEX DETECTION (FIXED) ----------
                        for (line in lines) {
                            val l = line.lowercase().trim()

                            if (detectedSex == null) {
                                when {
                                    l.contains("female") || l == "f" || l.endsWith(" f") ->
                                        detectedSex = "Female"

                                    l.contains("male") || l == "m" || l.endsWith(" m") ->
                                        detectedSex = "Male"
                                }
                            }
                        }

// ---------- AADHAAR DETECTION ----------
                        for (line in lines) {
                            val digits = line.replace(" ", "")
                            if (digits.matches(Regex("""\d{12}"""))) {
                                detectedAadhaar = digits
                                break
                            }
                        }
                        // ---------- PLACE / VILLAGE DETECTION ----------
                        var detectedPlace: String? = null

                        for (line in lines) {
                            val clean = line.trim()

                            if (
                                clean.contains("village", true) ||
                                clean.contains("district", true) ||
                                clean.contains("mandal", true) ||
                                clean.contains("taluk", true)
                            ) {
                                detectedPlace = clean
                                break
                            }
                        }

// ---------- NAME DETECTION (SIMPLE & SAFE) ----------
                        for (line in lines) {
                            val clean = line.trim()

                            if (clean.any { it.isDigit() }) continue
                            if (clean.length < 3) continue

                            val blocked = listOf(
                                "government", "india", "republic",
                                "authority", "unique", "identification",
                                "male", "female", "dob", "year"
                            )
                            if (blocked.any { clean.lowercase().contains(it) }) continue

                            if (!clean.matches(Regex("""[A-Za-z ]+"""))) continue

                            detectedName = clean
                            break
                        }

// ---------- AUTO-FILL (ONLY IF EMPTY) ----------

                        if (dob.isBlank() && detectedDob != null) {
                            dob = detectedDob
                        }

                        if (dob.isBlank() && detectedDob != null) {
                            dob = detectedDob
                        }

                        if (sex.isBlank() && detectedSex != null) {
                            sex = detectedSex
                        }

                        if (childId.isBlank() && detectedAadhaar != null) {
                            childId = detectedAadhaar
                        }

                        if (place.isBlank() && detectedPlace != null) {
                            place = detectedPlace
                        }
                        isAutoFilled = true

                        Toast.makeText(
                            context,
                            "Details auto-filled from card",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d("OCR_PARSE", "Name: $detectedName")
                        Log.d("OCR_PARSE", "DOB: $detectedDob")
                        Log.d("OCR_PARSE", "Sex: $detectedSex")

                        Log.d("OCR_RESULT", ocrText)
                        Toast.makeText(
                            context,
                            "Scan successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        // (Auto-fill will come later)
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Scan failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                cameraLauncher.launch(null)
            } else {
                Toast.makeText(
                    context,
                    "Camera permission required to scan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF3FB))// soft modern background
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    )
    {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                PrimaryBlue,
                                PrimaryBlueDark
                            )
                        )
                    )
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Malnutrition Risk Prediction",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            TopStepper(currentStep = 1)

            Spacer(modifier = Modifier.height(6.dp))
            Card(
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Child Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = PrimaryBlueDark
                    )
                    OutlinedTextField(
                        value = childName,
                        onValueChange = { childName = it },
                        label = { Text("Child Name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = Color(0xFFB0BEC5)
                        )
                    )
                    val context = LocalContext.current

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        OutlinedTextField(
                            value = dob,
                            onValueChange = { dob = it },
                            label = { Text("Date of Birth (DD-MM-YYYY)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBlue,
                                unfocusedBorderColor = Color(0xFFB0BEC5)
                            )
                        )

                        IconButton(
                            onClick = {

                                val calendar = java.util.Calendar.getInstance()

                                android.app.DatePickerDialog(
                                    context,
                                    { _, year, month, day ->
                                        dob = "%02d-%02d-%04d".format(day, month + 1, year)
                                    },
                                    calendar.get(java.util.Calendar.YEAR),
                                    calendar.get(java.util.Calendar.MONTH),
                                    calendar.get(java.util.Calendar.DAY_OF_MONTH)
                                ).show()

                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select Date"
                            )
                        }
                    }
                    LaunchedEffect(dob) {
                        try {
                            if (dob.isNotBlank()) {
                                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                val birthDate = sdf.parse(dob)

                                birthDate?.let {
                                    val diffMillis = Date().time - it.time
                                    val ageInMonths =
                                        (diffMillis / (1000L * 60 * 60 * 24 * 30)).toInt()
                                    if (ageInMonths >= 0) {
                                        age = ageInMonths.toString()
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            // ignore invalid format
                        }
                    }
                    if (isAutoFilled) {
                        Text(
                            text = "Auto-filled from card. Please verify before proceeding.",
                            color = Color(0xFFEF6C00),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = childId,
                            onValueChange = { childId = it },
                            label = { Text("Aadhaar / ABHA ID (optional)") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBlue,
                                unfocusedBorderColor = Color(0xFFB0BEC5)
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    cameraLauncher.launch(null)
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            },
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(48.dp)
                                .background(
                                    color = PrimaryBlue,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Scan Card",
                                tint = Color.White
                            )
                        }
                    }
                    OutlinedTextField(
                        value = place,
                        onValueChange = { place = it },
                        label = { Text("Place / Village") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = Color(0xFFB0BEC5)
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Card(
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Health Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = PrimaryBlueDark
                    )
                    Divider(
                        modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
                        color = Color(0xFFE0E0E0)
                    )
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Age (months)") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Auto-filled from DOB or enter manually") },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = Color(0xFFB0BEC5)
                        )
                    )
                    if (ageError.isNotEmpty()) {
                        Text(ageError, color = Color.Red, fontSize = 12.sp)
                    }

                    NumericInputField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = "Weight (kg)",
                        placeholder = "e.g. 2 – 30 kg",
                        isDecimal = true
                    )
                    if (weightError.isNotEmpty()) {
                        Text(weightError, color = Color.Red, fontSize = 12.sp)
                    }

                    NumericInputField(
                        value = height,
                        onValueChange = { height = it },
                        label = "Height (cm)",
                        placeholder = "e.g. 45 – 120 cm",
                        isDecimal = true
                    )
                    if (heightError.isNotEmpty()) {
                        Text(heightError, color = Color.Red, fontSize = 12.sp)
                    }

                    NumericInputField(
                        value = muac,
                        onValueChange = { muac = it },
                        label = "MUAC (cm)",
                        placeholder = "e.g. 8 – 20 cm",
                        isDecimal = true
                    )
                    if (muacError.isNotEmpty()) {
                        Text(muacError, color = Color.Red, fontSize = 12.sp)
                    }
                    NumericInputField(
                        value = hemoglobin,
                        onValueChange = { hemoglobin = it },
                        label = "Hemoglobin",
                        placeholder = "e.g. 5 – 18 g/dL",
                        isDecimal = true
                    )
                    if (hbError.isNotEmpty()) {
                        Text(hbError, color = Color.Red, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Card(
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                text = "Sex",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                WealthRadio("Male", sex) { sex = it }
                WealthRadio("Female", sex) { sex = it }
            }

                Dropdown(
                        "Mother Education",
                        listOf("NoEducation", "Primary", "Higher"),
                        motherEdu
                    ) { motherEdu = it }

                    Dropdown(
                        "Water Source",
                        listOf("Unsafe", "Protected Well", "Tap"),
                        waterSource
                    ) { waterSource = it }

                    Dropdown(
                        "Toilet",
                        listOf("Open", "Shared", "Private"),
                        toilet
                    ) { toilet = it }

                    Text(
                        text = "Wealth Index",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        WealthRadio("Poor", wealthIndex) { wealthIndex = it }
                        WealthRadio("Medium", wealthIndex) { wealthIndex = it }
                        WealthRadio("Rich", wealthIndex) { wealthIndex = it }
                    }

                    NumericInputField(
                        value = householdSize,
                        onValueChange = { householdSize = it },
                        label = "Household Size"
                    )

                    NumericInputField(
                        value = birthWeight,
                        onValueChange = { birthWeight = it },
                        label = "Birth Weight (gms)",
                        isDecimal = true
                    )
                }
            }
        }
        if (generalError.isNotEmpty()) {
            Text(
                generalError,
                color = Color.Red,
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = onBack,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, PrimaryBlue)
            )
            {
                Text("Back")
            }
            Button(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                elevation = ButtonDefaults.buttonElevation(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue
                ),
                onClick = {
                    isLoading = true

                    ageError = ""
                    weightError = ""
                    heightError = ""
                    muacError = ""
                    hbError = ""
                    generalError = ""

// Empty checks

                    if (sex.isBlank()) {
                        generalError = "Please select Sex"
                        isLoading = false
                        return@Button
                    }

                    if (motherEdu.isBlank()) {
                        generalError = "Please select Mother Education"
                        isLoading = false
                        return@Button
                    }

                    if (waterSource.isBlank()) {
                        generalError = "Please select Water Source"
                        isLoading = false
                        return@Button
                    }

                    if (toilet.isBlank()) {
                        generalError = "Please select Toilet Facility"
                        isLoading = false
                        return@Button
                    }

                    if (wealthIndex.isBlank()) {
                        generalError = "Please select Wealth Index"
                        isLoading = false
                        return@Button
                    }

                    if (householdSize.isBlank()) {
                        generalError = "Please enter Household Size"
                        isLoading = false
                        return@Button
                    }

                    if (birthWeight.isBlank()) {
                        generalError = "Please enter Birth Weight"
                        isLoading = false
                        return@Button
                    }
                    if (age.isBlank()) ageError = "Required"
                    if (weight.isBlank()) weightError = "Required"
                    if (height.isBlank()) heightError = "Required"
                    if (muac.isBlank()) muacError = "Required"
                    if (hemoglobin.isBlank()) hbError = "Required"

                    if (
                        ageError.isNotEmpty() ||
                        weightError.isNotEmpty() ||
                        heightError.isNotEmpty() ||
                        muacError.isNotEmpty() ||
                        hbError.isNotEmpty()
                    ) {
                        isLoading = false     // ✅ REQUIRED FIX
                        return@Button
                    }

                    // ✅ PASTED VALIDATION CODE (HERE)
                    val validationError = validateInputs(
                        age = age,
                        weight = weight,
                        height = height,
                        muac = muac,
                        hemoglobin = hemoglobin,
                        householdSize = householdSize,
                        birthWeight = birthWeight
                    )
                    if (validationError != null) {
                        isLoading = false   // ✅ IMPORTANT FIX
                        generalError = validationError
                        return@Button
                    }
                    val wealthIndexNumeric = when (wealthIndex) {
                        "Poor" -> 0
                        "Medium" -> 1
                        "Rich" -> 2
                        else -> 0
                    }
                    val request = PatientRequest(
                        age = age.toInt(),
                        weight = weight.toDouble(),
                        height = height.toIntOrNull() ?: 0,
                        muac = muac.toDouble(),
                        hemoglobin = hemoglobin.toDouble(),
                        sex = sex,
                        mother_edu = motherEdu,
                        water_source = waterSource,
                        toilet = toilet,
                        wealth_index = wealthIndexNumeric,
                        household_size = householdSize.toInt(),
                        birth_weight = birthWeight.toDouble()
                    )

                    ApiClient.api.predictRisk(request)
                        .enqueue(object : Callback<PredictionResponse> {

                            override fun onResponse(
                                call: Call<PredictionResponse>,
                                response: Response<PredictionResponse>
                            ) {
                                isLoading = false
                                if (!response.isSuccessful) {
                                    generalError = "Server error: ${response.code()}"
                                    return
                                }
                                val body = response.body()
                                val result = body ?: return
                                if (body == null) {
                                    generalError = "Empty response from server"
                                    return
                                }

// 🔥 THEN navigate / call onResult
                                onResult(
                                    childName,
                                    childId,
                                    place,
                                    request,
                                    body
                                )
                            }

                            override fun onFailure(
                                call: Call<PredictionResponse>,
                                t: Throwable
                            ) {
                                isLoading = false
                                generalError = "Network error: ${t.message}"
                            }
                        })
                }
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text("Predict")
                }
            }
        }
    }
}

// -------------------- RESULT SCREEN --------------------
@Composable
fun ResultScreen(
    risk: String,
    dietMessage: String,
    onBack: () -> Unit,
    onEdit: () -> Unit      // ✅ ADD
) {

    val safeRisk = normalizeRisk(risk)

    val subRisk = when (safeRisk) {
        "Low" -> "Mild"
        "Medium" -> "Moderate"
        "High" -> "Severe"
        else -> "Normal"
    }

    val riskColor = when (safeRisk) {
        "High" -> RiskHigh
        "Medium" -> RiskMedium
        "Low" -> RiskLow
        else -> RiskLow
    }

    val riskBg = when (safeRisk) {
        "High" -> RiskHighBg
        "Medium" -> RiskMediumBg
        "Low" -> RiskLowBg
        else -> RiskLowBg
    }

    val dietImage = when (safeRisk) {
        "High" -> R.drawable.diet_high
        "Medium" -> R.drawable.diet_medium
        "Low" -> R.drawable.diet_low
        "Normal" -> R.drawable.diet_normal
        else -> R.drawable.diet_common
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopStepper(
            currentStep = 2,
            onStepClick = { step ->
                if (step == 1) onEdit()
            }
        )
        // 🔹 Title
        Text(
            text = "Risk Assessment",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Risk Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = riskBg),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = risk,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = riskColor,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Malnutrition Risk Status",
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Severity: $subRisk",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = riskColor
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Risk Indicator Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "RISK INDICATOR",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
                RiskMeterGauge(risk = safeRisk)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Diet Advice
        Text(
            text = dietMessage,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Diet Images (Zoom animated)
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            ClickToPreviewImage(R.drawable.diet_common)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            ClickToPreviewImage(dietImage)
        }

        Spacer(modifier = Modifier.height(28.dp))

        PressableButton(
            text = "Back to Home",
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// -------------------- RISK SCALE --------------------

@Composable
fun RiskScale(risk: String) {

    val safeRisk = normalizeRisk(risk)

    val index = when (safeRisk) {
        "Normal" -> 0
        "Low" -> 1
        "Medium" -> 2
        "High" -> 3
        else -> -1
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            RiskBox("Normal", Color.Green)
            RiskBox("Low", Color(0xFFFFC107))
            RiskBox("Medium", Color(0xFFFF9800))
            RiskBox("High", Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(4) {
                if (it == index) Text("⬆", fontSize = 22.sp) else Text(" ")
            }
        }
    }
}

@Composable
fun RiskBox(label: String, color: Color) {
    Box(
        modifier = Modifier
            .width(70.dp)
            .height(40.dp)
            .background(color, shape = MaterialTheme.shapes.small),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = Color.White, fontSize = 12.sp)
    }
}

// -------------------- DROPDOWN --------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
        )

        ExposedDropdownMenu(expanded, { expanded = false }) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelect(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun NumericInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    isDecimal: Boolean = false
) {
    Column {

        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )

        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                .padding(12.dp),

            factory = { context ->
                android.widget.EditText(context).apply {
                    hint = placeholder
                    inputType =
                        if (isDecimal)
                            InputType.TYPE_CLASS_NUMBER or
                                    InputType.TYPE_NUMBER_FLAG_DECIMAL
                        else
                            InputType.TYPE_CLASS_NUMBER
                }
            },

            update = { editText ->

                if (editText.text.toString() != value) {
                    editText.setText(value)
                    editText.setSelection(editText.text.length)
                }

                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?, start: Int, count: Int, after: Int
                    ) {}

                    override fun onTextChanged(
                        s: CharSequence?, start: Int, before: Int, count: Int
                    ) {
                        onValueChange(s.toString())
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })
            }
        )
    }
}


@Composable
fun RiskPieChart(records: List<PredictionRecord>) {

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(16.dp)
    ) {

        val total = records.size.toFloat().coerceAtLeast(1f)

        val high = records.count { it.risk == "High" }
        val medium = records.count { it.risk == "Medium" }
        val low = records.count { it.risk == "Low" }
        val normal = records.count { it.risk == "Normal" }

        val slices = listOf(
            Pair(high, Color.Red),
            Pair(medium, Color(0xFFFF9800)),
            Pair(low, Color(0xFFFFC107)),
            Pair(normal, Color.Green)
        )

        var startAngle = -90f

        slices.forEach { (count, color) ->
            if (count > 0) {
                val sweep = (count / total) * 360f

                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true,
                    size = this.size
                )

                startAngle += sweep
            }
        }
    }
}

@Composable
fun RiskLegend() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        LegendRow(color = Color.Green, label = "Normal")
        LegendRow(color = Color(0xFFFFC107), label = "Low")
        LegendRow(color = Color(0xFFFF9800), label = "Medium")
        LegendRow(color = Color.Red, label = "High")
    }
}

@Composable
fun LegendRow(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = label, fontSize = 14.sp)
    }
}

@Composable
fun NearbyScreen(onBack: () -> Unit) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🔹 TITLE
        Text(
            text = "Nearby Health Facilities",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 DESCRIPTION
        Text(
            text = "Find nearby hospitals, clinics, and primary health centers using Google Maps.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            AdvancedLocationAnimation()
        }

        // 🔹 OPEN MAPS BUTTON
        Button(
            onClick = {
                val uri = Uri.parse("geo:0,0?q=hospitals near me")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp)
        ) {
            Text("Open Nearby Hospitals")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 BACK BUTTON
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp)
        ) {
            Text("Back")
        }
    }
}

@Composable
fun PulsingLocationAnimation() {

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    Box(
        modifier = Modifier
            .size(220.dp),
        contentAlignment = Alignment.Center
    ) {

        // Outer pulse
        Box(
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                }
                .background(Color(0xFF009688), CircleShape)
        )

        // Inner location dot
        Box(
            modifier = Modifier
                .size(26.dp)
                .background(Color(0xFF00695C), CircleShape)
        )
    }
}

@Composable
fun AdvancedLocationAnimation() {

    val infiniteTransition = rememberInfiniteTransition(label = "")

    // Rotation animation for icons
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    // Pulse animation
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    Box(
        modifier = Modifier.size(260.dp),
        contentAlignment = Alignment.Center
    ) {

        // 🔵 Outer pulse ring
        Box(
            modifier = Modifier
                .size(240.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                }
                .background(Color(0xFF80CBC4), CircleShape)
        )

        // 🔄 Rotating icons ring
        Box(
            modifier = Modifier
                .size(220.dp)
                .graphicsLayer { rotationZ = rotation }
        ) {

            OrbitIcon("🏥", Alignment.TopCenter)
            OrbitIcon("💊", Alignment.CenterEnd)
            OrbitIcon("🚑", Alignment.BottomCenter)
            OrbitIcon("🧪", Alignment.CenterStart)
        }

        // 📍 Center location dot
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(Color(0xFF00695C), CircleShape)
        )
    }
}
@Composable
fun OrbitIcon(icon: String, alignment: Alignment) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = alignment
    ) {
        Text(
            text = icon,
            fontSize = 26.sp
        )
    }
}
@Composable
fun ProfileScreen(
    worker: HealthWorker,
    totalRecords: Int,
    onBack: () -> Unit
) {
    val username = "Health Worker"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .statusBarsPadding()
    ) {

        // 🔹 HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF00796B),
                            Color(0xFF009688)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Account",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Username: $username",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Role: Health Worker Job",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 INFO CARDS
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ProfileInfoCard("Worker ID", worker.id, "🆔")
            ProfileInfoCard("Village", worker.village, "📍")
            ProfileInfoCard("Total Records", totalRecords.toString(), "📊")
        }

        Spacer(modifier = Modifier.weight(1f))

        // 🔹 BACK BUTTON
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding(),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Back")
        }
    }
}

@Composable
fun ProfileInfoCard(title: String, value: String, icon: String) {

    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(600),
        label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(icon, fontSize = 26.sp)

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(title, color = Color.Gray, fontSize = 13.sp)
                Text(
                    value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


@Composable
fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun RiskMeterGauge(risk: String) {

    // 🎯 Map risk → needle angle (semi-circle: 180° → 360°)
    val targetAngle = when (risk) {
        "Normal" -> 210f
        "Low" -> 230f
        "Medium" -> 270f
        "High" -> 320f
        else -> 210f
    }

    val animatedAngle by animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {

            val strokeWidth = 22f
            val radius = size.minDimension / 2.1f
            val center = Offset(size.width / 2, size.height / 1.3f)

            // 🟢 LOW (Green)
            drawArc(
                color = RiskLow,
                startAngle = 180f,
                sweepAngle = 60f,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )

            // 🟡 MEDIUM (Yellow)
            drawArc(
                color = RiskMedium,
                startAngle = 240f,
                sweepAngle = 60f,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )

            // 🔴 HIGH (Red)
            drawArc(
                color = RiskHigh,
                startAngle = 300f,
                sweepAngle = 60f,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )

            // 🧭 Needle calculation
            val angleRad = Math.toRadians(animatedAngle.toDouble())
            val needleLength = radius * 0.85f

            val needleEnd = Offset(
                x = center.x + needleLength * cos(angleRad).toFloat(),
                y = center.y + needleLength * sin(angleRad).toFloat()
            )

            // Needle line
            drawLine(
                color = Color.DarkGray,
                start = center,
                end = needleEnd,
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )

            // 🔺 Pointy needle head
            val arrowSize = 10f
            val arrowAngle = Math.atan2(
                (needleEnd.y - center.y).toDouble(),
                (needleEnd.x - center.x).toDouble()
            )

            val left = Offset(
                needleEnd.x - arrowSize * cos(arrowAngle - Math.PI / 6).toFloat(),
                needleEnd.y - arrowSize * sin(arrowAngle - Math.PI / 6).toFloat()
            )

            val right = Offset(
                needleEnd.x - arrowSize * cos(arrowAngle + Math.PI / 6).toFloat(),
                needleEnd.y - arrowSize * sin(arrowAngle + Math.PI / 6).toFloat()
            )

            drawPath(
                path = Path().apply {
                    moveTo(needleEnd.x, needleEnd.y)
                    lineTo(left.x, left.y)
                    lineTo(right.x, right.y)
                    close()
                },
                color = Color.DarkGray
            )

            // Center knob
            drawCircle(
                color = Color.DarkGray,
                radius = 6f,
                center = center
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("LOW", fontSize = 12.sp, color = RiskLow)
            Text("MEDIUM", fontSize = 12.sp, color = RiskMedium)
            Text("HIGH", fontSize = 12.sp, color = RiskHigh)
        }
    }
}

@Composable
fun WealthRadio(
    label: String,
    selectedValue: String,
    onSelect: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onSelect(label) }
    ) {
        RadioButton(
            selected = selectedValue == label,
            onClick = { onSelect(label) }
        )

        Text(
            text = label,
            fontSize = 15.sp
        )
    }
}
@Composable
fun TopStepper(
    currentStep: Int,
    onStepClick: (Int) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        StepItem(1, "Predict", currentStep) {
            onStepClick(1)
        }

        StepDivider(currentStep)

        StepItem(2, "Result", currentStep) {
            onStepClick(2)
        }
    }
}
@Composable
fun StepItem(
    step: Int,
    label: String,
    currentStep: Int,
    onClick: (() -> Unit)? = null
) {

    val active = step == currentStep
    val completed = step < currentStep

    val bgColor by animateColorAsState(
        if (active) PrimaryBlue else if (completed) Color(0xFF4CAF50) else Color.LightGray,
        label = ""
    )

    val scale by animateFloatAsState(
        if (active) 1.15f else 1f,
        label = ""
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(enabled = onClick != null) {
            onClick?.invoke()
        }
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = if (completed) "✓" else step.toString(),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = label,
            fontSize = 12.sp,
            color = if (active) PrimaryBlue else Color.Gray
        )
    }
}

@Composable
fun StepDivider(currentStep: Int) {

    val progress by animateFloatAsState(
        targetValue = if (currentStep > 1) 1f else 0f,
        animationSpec = tween(500),
        label = ""
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .height(2.dp)
            .width(80.dp)
            .background(Color.LightGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .background(PrimaryBlue)
        )
    }
}

@Composable
fun ZoomImage(imageRes: Int) {

    val scale by rememberInfiniteTransition().animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    )
}
@Composable
fun PressableButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(150),
        label = ""
    )

    Button(
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
        Text(text)
    }
}

@Composable
fun ClickToPreviewImage(imageRes: Int) {

    var showFullScreen by remember { mutableStateOf(false) }

    // Normal image (inside Result screen)
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { showFullScreen = true }
    )

    // Full screen popup
    if (showFullScreen) {
        Dialog(onDismissRequest = { showFullScreen = false }) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                // Close hint
                Text(
                    text = "Tap anywhere to close",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                )

                // Close on tap
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showFullScreen = false }
                )
            }
        }
    }
}

fun exportFullReport(context: Context, record: PredictionRecord): Uri? {
    return try {

        val fileName = "${record.childName}_Full_Report.pdf"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(
            MediaStore.Files.getContentUri("external"),
            contentValues
        ) ?: return null

        val outputStream = resolver.openOutputStream(uri) ?: return null

        val writer = PdfWriter(outputStream)
        val pdf = PdfDocument(writer)
        val document = Document(pdf)

        document.add(Paragraph("Child Malnutrition Risk Report"))
        document.add(Paragraph("-------------------------------------"))
        document.add(Paragraph(""))

        document.add(Paragraph("Child Name: ${record.childName}"))
        document.add(Paragraph("Aadhaar / ABHA ID: ${record.childId}"))
        document.add(Paragraph("Place/Village: ${record.place}"))
        document.add(Paragraph("Recorded On: ${record.dateTime}"))
        document.add(Paragraph(""))

        document.add(Paragraph("----- HEALTH DETAILS -----"))
        document.add(Paragraph("Age (months): ${record.age}"))
        document.add(Paragraph("Weight (kg): ${record.weight}"))
        document.add(Paragraph("Height (cm): ${record.height}"))
        document.add(Paragraph("MUAC (cm): ${record.muac}"))
        document.add(Paragraph("Hemoglobin (g/dL): ${record.hemoglobin}"))
        document.add(Paragraph("Birth Weight (g): ${record.birthweight}"))
        document.add(Paragraph(""))

        document.add(Paragraph("----- SOCIO-ECONOMIC DETAILS -----"))
        document.add(Paragraph("Sex: ${record.sex}"))
        document.add(Paragraph("Mother Education: ${record.motherEdu}"))
        document.add(Paragraph("Wealth Index: ${record.wealthIndex}"))
        document.add(Paragraph("Household Size: ${record.householdsize}"))
        document.add(Paragraph("Water Source: ${record.waterSource}"))
        document.add(Paragraph("Toilet Facility: ${record.toilet}"))
        document.add(Paragraph(""))

        document.add(Paragraph("----- PREDICTION RESULT -----"))
        document.add(Paragraph("Predicted Risk Level: ${record.risk}"))

        document.close()
        outputStream.close()

        uri

    } catch (e: Exception) {
        null
    }
}
fun openPdf(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show()
    }
}