package com.example.lab4
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lab4.ui.theme.Lab4Theme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreenWithNav()
        }
    }
}

@Composable
fun MainScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    val palette = LocalPalette.current
    val selectedMeditation by sharedViewModel.selectedItem.collectAsState()
    val userName by sharedViewModel.userName.collectAsState()
    var txt = "[Имя]"
    if(userName !="")
        txt = userName
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


        Text(text = "Добро пожаловать, $txt!", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(72.dp))

        Button(
            onClick = {
                if (selectedMeditation != null) {
                    navController.navigate("meditation/${selectedMeditation!!.id}")
                } else {
                    println("Медитация не выбрана")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = palette.secondary),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Начать Медитацию", color = Color.White)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun MeditationLibraryScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    repository: MeditationRepository,
    viewModel: MeditationLibraryViewModel = viewModel(factory = MeditationViewModelFactory(repository))
) {
    val palette = LocalPalette.current
    val meditations by viewModel.meditations.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedMeditation by sharedViewModel.selectedItem.collectAsState()
    val meditationDetails by viewModel.meditationDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    //Детали об медитации
    if (meditationDetails != null) {
        MeditationDetailsDialog(
            meditation = meditationDetails!!,
            palette = palette,
            onDismiss = { viewModel.clearSelectedMeditation() }
        )
    }

    // Загружаем медитации при првом открытии экрана
    LaunchedEffect(Unit) {
        viewModel.loadMeditations()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Поле поиска
        TextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Поиск...") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Индикатор загрузки
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        // Сообщение об ошибке
        else if (error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error!!,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }
        // Список медитаций
        else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(viewModel.getFilteredMeditations()) { meditation ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Блок медитации
                        MeditationItem(
                            meditation = meditation,
                            isSelected = selectedMeditation?.id == meditation.id,
                            onSelected = {
                                viewModel.selectMeditation(meditation)
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                viewModel.onMeditationSelected(meditation)
                                sharedViewModel.selectItem(meditation)
                                navController.navigate("meditation/${meditation.id}")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = palette.primary)
                        ) {
                            Text(text = "Начать")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MeditationItem(
    meditation: APIMeditation,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val palette = LocalPalette.current
    val backgroundColor = if (isSelected) palette.secondary else Color.LightGray

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable { onSelected() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.LightGray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "*Тут должна была быть картинка*",
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = meditation.title,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun MeditationDetailsDialog(
    meditation: APIMeditationDetail,
    palette: AppPalette,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = meditation.title) },
        text = {
            Column {
                Text(text = "Описание: ${meditation.description}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Длительность: ${meditation.duration} мин")
            }
        },
        confirmButton = {
            Button(onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = palette.secondary),) {
                Text("Закрыть")
            }
        }
    )
}
@Composable
fun MusicScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    val palette = LocalPalette.current
    val selectedMeditation by sharedViewModel.selectedItem.collectAsState()
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
            if (selectedMeditation != null) {
                MeditationItem(
                    meditation = selectedMeditation!!,
                    isSelected = true,
                    onSelected = { /* Ничего не делаем, так как это экран музыки */ }
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(160.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                )
            }
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
                onClick = {
                    if (selectedMeditation != null) {
                        navController.navigate("meditation/${selectedMeditation!!.id}")
                    } else {
                        println("Медитация не выбрана")
                    }
                },
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
    onPaletteChange: (PaletteOption) -> Unit,
    sharedViewModel: SharedViewModel,
    profileViewModel: ProfileViewModel
) {
    val name by profileViewModel.name.collectAsState()
    val email by profileViewModel.email.collectAsState()
    val remindMeditation by profileViewModel.remindMeditation.collectAsState()
    val notifyNewMeditations by profileViewModel.notifyNewMeditations.collectAsState()
    val notifyNewMusic by profileViewModel.notifyNewMusic.collectAsState()

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
            onValueChange = { profileViewModel.updateName(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Имя") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { profileViewModel.updateEmail(it) },
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
                onCheckedChange = { profileViewModel.updateRemindMeditation(it) },
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
                onCheckedChange = { profileViewModel.updateNotifyNewMeditations(it) },
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
                onCheckedChange = { profileViewModel.updateNotifyNewMusic(it) },
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                sharedViewModel.updateUserName(name)
            },
            colors = ButtonDefaults.buttonColors(containerColor = LocalPalette.current.secondary),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Сохранить", color = Color.White)
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

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MeditationScreen(
    navController: NavController,
    meditationId: String?,
    viewModel: MeditationLibraryViewModel = viewModel()
) {
    val palette = LocalPalette.current

    val selectedMeditation = viewModel.meditations.value.find { it.id == meditationId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.primary)
            .padding(16.dp)
    ) {
        Text(
            text = "Медитация",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (selectedMeditation != null) {
            MeditationItem(
                meditation = selectedMeditation,
                isSelected = true,
                onSelected = { /* Ничего не делаем, так как это экран медитации */ }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "0:00",
            fontSize = 48.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = palette.secondary),
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
    sharedViewModel: SharedViewModel,
    onPaletteChange: (PaletteOption) -> Unit,
    userPreferences: UserPreferences
) {
    NavHost(navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController, sharedViewModel)
        }
        composable("library") {
            val apiService = RetrofitClient.instance
            val repository = MeditationRepository(apiService)

            MeditationLibraryScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                repository = repository
            )

        }
        composable("music") {
            MusicScreen(navController, sharedViewModel)
        }
        composable("progress") {
            ProgressScreen(navController)
        }
        composable("profile") { backStackEntry ->
            val profileViewModel: ProfileViewModel = viewModel(
                factory = ProfileViewModelFactory(userPreferences)
            )
            ProfileScreen(
                navController = navController,
                onPaletteChange = onPaletteChange,
                sharedViewModel = sharedViewModel,
                profileViewModel = profileViewModel
            )
        }
        composable(
            route = "meditation/{meditationId}",
            arguments = listOf(navArgument("meditationId") { type = NavType.IntType })
        ) { backStackEntry ->
            val meditationId = backStackEntry.arguments?.getString("meditationId")
            MeditationScreen(navController, meditationId)
        }
    }
}
class SharedViewModel : ViewModel() {
    private val _selectedItem = MutableStateFlow<APIMeditation?>(null)
    val selectedItem: StateFlow<APIMeditation?> get() = _selectedItem

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> get() = _userName

    fun selectItem(item: APIMeditation?) {
        _selectedItem.value = item
    }

    fun updateUserName(newName: String) {
        _userName.value = newName
    }
}

@Composable
fun MainScreenWithNav() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()
    val context = LocalContext.current
    val userPreferences = UserPreferences(context.dataStore)
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
                if (!currentRoute.contentEquals("meditation/")) {
                    BottomNavigationBar(navController)
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavigationGraph(
                    navController = navController,
                    currentPalette = currentPalette,
                    sharedViewModel = sharedViewModel,
                    onPaletteChange = { newPalette ->
                        currentPalette = newPalette
                    },
                    userPreferences = userPreferences
                )
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