package com.example.lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lab4.ui.theme.Lab4Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreenWithNav()
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    val palette = LocalPalette.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Вы активны уже N дней!", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        Text(text = "Добро пожаловать, [Имя]!", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(72.dp))

        Button(
            onClick = { navController.navigate("meditation") },
            colors = ButtonDefaults.buttonColors(containerColor = palette.secondary),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Начать Медитацию", color = Color.White)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun MeditationLibraryScreen(navController: NavController) {
    val palette = LocalPalette.current
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Поиск...") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            repeat(3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(2) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = { navController.navigate("meditation") },
                                colors = ButtonDefaults.buttonColors(containerColor = palette.secondary)
                            ) {
                                Text(text = "Начать")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun MusicScreen(navController: NavController) {
    val palette = LocalPalette.current
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(2) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(2) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(2) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                )
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(160.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "0:00", fontSize = 16.sp, modifier = Modifier.align(Alignment.CenterHorizontally))

        Slider(value = 0f, onValueChange = {})

        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.navigate("meditation") },
                colors = ButtonDefaults.buttonColors(containerColor = palette.secondary),
                shape = RoundedCornerShape(8.dp)

            ) {
                Text(text = "Начать Медитацию")
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun ProgressScreen(navController: NavController) {
    val palette = LocalPalette.current
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.height(64.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(64.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "[N] дней с нами!")
            }

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Уже [N] часов медитации!")
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Вы любите [MedName]")
            }

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Вам нравится [MusName]")
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
@Composable
fun ProfileScreen(
    navController: NavController,
    onPaletteChange: (PaletteOption) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var remindMeditation by remember { mutableStateOf(false) }
    var notifyNewMeditations by remember { mutableStateOf(false) }
    var notifyNewMusic by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Профиль", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Имя") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Настройки уведомлений", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Напоминать о медитации?")
            Switch(
                checked = remindMeditation,
                onCheckedChange = { remindMeditation = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = LocalPalette.current.secondary,
                    checkedTrackColor = LocalPalette.current.primary,
                    uncheckedThumbColor = LocalPalette.current.primary,
                    uncheckedTrackColor = LocalPalette.current.secondary
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Уведомлять о новых медитациях?")
            Switch(
                checked = notifyNewMeditations,
                onCheckedChange = { notifyNewMeditations = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = LocalPalette.current.secondary,
                    checkedTrackColor = LocalPalette.current.primary,
                    uncheckedThumbColor = LocalPalette.current.primary,
                    uncheckedTrackColor = LocalPalette.current.secondary
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Уведомлять о новой музыке?")
            Switch(
                checked = notifyNewMusic,
                onCheckedChange = { notifyNewMusic = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = LocalPalette.current.secondary,
                    checkedTrackColor = LocalPalette.current.primary,
                    uncheckedThumbColor = LocalPalette.current.primary,
                    uncheckedTrackColor = LocalPalette.current.secondary
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            ColorCircle(PaletteOption.Option1, onPaletteChange)
            ColorCircle(PaletteOption.Option2, onPaletteChange)
            ColorCircle(PaletteOption.Option3, onPaletteChange)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun ColorCircle(option: PaletteOption, onPaletteChange: (PaletteOption) -> Unit) {
    val colors = when (option) {
        PaletteOption.Option1 -> Pair(Color(0xFFDEAFDB), Color(0xFFCC6FC6))
        PaletteOption.Option2 -> Pair(Color(0xFF95ECA8), Color(0xFF6AC17D))
        PaletteOption.Option3 -> Pair(Color(0xFFFFBE96), Color(0xFFEE8D50))
    }
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(colors.first, shape = CircleShape)
            .clickable {
                onPaletteChange(option)
            }
            .border(5.dp, colors.second, shape = CircleShape)
    )
}

data class AppPalette(
    val primary: Color,
    val secondary: Color
)

enum class PaletteOption {
    Option1, Option2, Option3
}
val LocalPalette = staticCompositionLocalOf {
    AppPalette(
        primary = Color(0xFFDEAFDB),
        secondary = Color(0xFFCC6FC6)
    )
}
@Composable
fun MeditationScreen(navController: NavController) {
    val palette = LocalPalette.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.primary) // Фон экрана медитации
            .padding(16.dp)
    ) {
        Text(text = "Медитация", fontSize = 24.sp, modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "0:00", fontSize = 48.sp, modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = palette.secondary), // Активный цвет
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Прервать Медитацию", color = Color.White)
        }
    }
}
@Composable
fun NavigationGraph(
    navController: NavHostController,
    currentPalette: PaletteOption,
    onPaletteChange: (PaletteOption) -> Unit
) {
    NavHost(navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("library") { MeditationLibraryScreen(navController) }
        composable("music") { MusicScreen(navController) }
        composable("progress") { ProgressScreen(navController) }
        composable("profile") { ProfileScreen(navController, onPaletteChange) }
        composable("meditation") { MeditationScreen(navController) }
    }
}

@Composable
fun MainScreenWithNav() {
    val navController = rememberNavController()
    var currentPalette by remember { mutableStateOf(PaletteOption.Option1) }
    val currentRoute = currentRoute(navController)

    val palette = when (currentPalette) {
        PaletteOption.Option1 -> AppPalette(
            primary = Color(0xFFDEAFDB),
            secondary = Color(0xFFCC6FC6)
        )
        PaletteOption.Option2 -> AppPalette(
            primary = Color(0xFF95ECA8),
            secondary = Color(0xFF6AC17D)
        )
        PaletteOption.Option3 -> AppPalette(
            primary = Color(0xFFFFBE96),
            secondary = Color(0xFFEE8D50)
        )
    }

    CompositionLocalProvider(LocalPalette provides palette) {
        Scaffold(
            bottomBar = {
                // BottomNavigationBar только если текущий экран НЕ "медитация"
                if (currentRoute != "meditation") {
                    BottomNavigationBar(navController)
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavigationGraph(navController, currentPalette) { newPalette ->
                    currentPalette = newPalette
                }
            }
        }
    }
}
@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    return navBackStackEntry?.destination?.route
}
@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = currentRoute(navController)
    val palette = LocalPalette.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(palette.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = { navController.navigate("main") },
                modifier = Modifier
                    .background(if (currentRoute == "main") palette.secondary else Color.Transparent, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(text = "🏠", color = if (currentRoute == "main") Color.White else Color.Black)
            }

            IconButton(
                onClick = { navController.navigate("library") },
                modifier = Modifier
                    .background(if (currentRoute == "library") palette.secondary else Color.Transparent, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(text = "📚", color = if (currentRoute == "library") Color.White else Color.Black)
            }

            IconButton(
                onClick = { navController.navigate("music") },
                modifier = Modifier
                    .background(if (currentRoute == "music") palette.secondary else Color.Transparent, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(text = "🎵", color = if (currentRoute == "music") Color.White else Color.Black)
            }

            IconButton(
                onClick = { navController.navigate("progress") },
                modifier = Modifier
                    .background(if (currentRoute == "progress") palette.secondary else Color.Transparent, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(text = "📊", color = if (currentRoute == "progress") Color.White else Color.Black)
            }

            IconButton(
                onClick = { navController.navigate("profile") },
                modifier = Modifier
                    .background(if (currentRoute == "profile") palette.secondary else Color.Transparent, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(text = "👤", color = if (currentRoute == "profile") Color.White else Color.Black)
            }
        }
    }
}