package za.co.mohlare.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import za.co.mohlare.app.data.SubjectMark
import za.co.mohlare.app.data.UserProfile
import za.co.mohlare.app.ui.auth.GoogleResult
import za.co.mohlare.app.ui.auth.clearGoogleSignIn
import za.co.mohlare.app.ui.auth.requestGoogleIdToken
import za.co.mohlare.app.ui.screens.EmailAuthScreen
import za.co.mohlare.app.ui.screens.FailedScreen
import za.co.mohlare.app.ui.screens.LoadingScreen
import za.co.mohlare.app.ui.screens.MarksScreen
import za.co.mohlare.app.ui.screens.OnboardingScreen
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import za.co.mohlare.app.ui.screens.AppearanceScreen
import za.co.mohlare.app.ui.screens.DiscoverScreen
import za.co.mohlare.app.ui.screens.MeScreen
import za.co.mohlare.app.ui.screens.PathScreen
import za.co.mohlare.app.ui.screens.PlanScreen
import za.co.mohlare.app.ui.screens.WelcomeScreen
import za.co.mohlare.app.ui.theme.Mohlare
import za.co.mohlare.app.ui.theme.MohlareThemeId

object Routes {
    const val PATH = "path"
    const val DISCOVER = "discover"
    const val PLAN = "plan"
    const val ME = "me"
    const val APPEARANCE = "appearance"
    const val MARKS = "marks"
}

private enum class Tab(val route: String, val label: String, val icon: ImageVector) {
    PATH(Routes.PATH, "Path", Icons.Filled.Home),
    DISCOVER(Routes.DISCOVER, "Discover", Icons.Filled.Search),
    PLAN(Routes.PLAN, "Plan", Icons.Filled.DateRange),
    ME(Routes.ME, "Me", Icons.Filled.Person),
}

/**
 * The whole app. Which part shows depends on [AppState]:
 * signed out → welcome and email sign-in; new account → profile setup; ready → the four tabs.
 */
@Composable
fun MohlareApp(
    currentTheme: MohlareThemeId,
    onThemeChange: (MohlareThemeId) -> Unit,
    language: String,
    onLanguageChange: (String) -> Unit,
    vm: AppViewModel = viewModel(),
) {
    val state by vm.state.collectAsState()
    val busy by vm.busy.collectAsState()
    val message by vm.message.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val signOut: () -> Unit = {
        vm.signOut()
        scope.launch { clearGoogleSignIn(context) }
    }

    // Screens outside the tabs keep clear of the status bar and the keyboard.
    val c = Mohlare.tokens.colors
    Box(
        Modifier
            .fillMaxSize()
            .background(c.background)
            .then(if (state is AppState.Ready) Modifier else Modifier.safeDrawingPadding())
    ) {
    when (val s = state) {
        AppState.Loading -> LoadingScreen()

        AppState.SignedOut -> {
            var showEmail by rememberSaveable { mutableStateOf(false) }
            if (showEmail) {
                BackHandler { showEmail = false; vm.clearMessage() }
                EmailAuthScreen(
                    busy = busy,
                    message = message,
                    onSignIn = vm::signInWithEmail,
                    onRegister = vm::registerWithEmail,
                    onForgotPassword = vm::sendPasswordReset,
                    onBack = { showEmail = false; vm.clearMessage() },
                )
            } else {
                WelcomeScreen(
                    language = language,
                    onLanguageChange = onLanguageChange,
                    busy = busy,
                    message = message,
                    onGoogle = {
                        vm.clearMessage()
                        scope.launch {
                            when (val result = requestGoogleIdToken(context)) {
                                is GoogleResult.Success -> vm.signInWithGoogle(result.idToken)
                                is GoogleResult.Error -> vm.showMessage(result.message)
                                GoogleResult.Cancelled -> Unit
                            }
                        }
                    },
                    onEmail = { showEmail = true; vm.clearMessage() },
                )
            }
        }

        is AppState.NeedsProfile -> OnboardingScreen(
            suggestedName = s.suggestedName,
            initialLanguage = language,
            busy = busy,
            message = message,
            onSave = { profile -> vm.saveProfile(s.uid, profile) },
            onSignOut = signOut,
        )

        is AppState.Ready -> MainTabs(
            profile = s.profile,
            currentTheme = currentTheme,
            onThemeChange = onThemeChange,
            busy = busy,
            message = message,
            onSaveMarks = { marks, term, onSaved -> vm.saveMarks(s.uid, marks, term, onSaved) },
            onClearMessage = vm::clearMessage,
            onSignOut = signOut,
            onDeleteAccount = { vm.deleteAccount(s.uid) },
        )

        is AppState.Failed -> FailedScreen(s.message, onSignOut = signOut)
    }
    }
}

/** The signed-in app: Path, Discover, Plan and Me. */
@Composable
private fun MainTabs(
    profile: UserProfile,
    currentTheme: MohlareThemeId,
    onThemeChange: (MohlareThemeId) -> Unit,
    busy: Boolean,
    message: String?,
    onSaveMarks: (marks: List<SubjectMark>, term: String, onSaved: () -> Unit) -> Unit,
    onClearMessage: () -> Unit,
    onSignOut: () -> Unit,
    onDeleteAccount: () -> Unit,
) {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route
    val c = Mohlare.tokens.colors

    Scaffold(
        containerColor = c.background,
        bottomBar = {
            if (Tab.entries.any { it.route == route }) {
                NavigationBar(containerColor = c.surface) {
                    Tab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = route == tab.route,
                            onClick = { nav.goToTab(tab.route) },
                            icon = { Icon(tab.icon, contentDescription = null) },
                            label = { Text(tab.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = c.ink,
                                selectedTextColor = c.ink,
                                indicatorColor = c.tint,
                                unselectedIconColor = c.muted,
                                unselectedTextColor = c.muted,
                            ),
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(nav, startDestination = Routes.PATH, modifier = Modifier.padding(padding)) {
            composable(Routes.PATH) {
                PathScreen(
                    profile = profile,
                    onUpdateMarks = { onClearMessage(); nav.navigate(Routes.MARKS) },
                    onSeePlan = { nav.goToTab(Routes.PLAN) },
                )
            }
            composable(Routes.DISCOVER) { DiscoverScreen() }
            composable(Routes.PLAN) { PlanScreen() }
            composable(Routes.ME) {
                MeScreen(
                    profile = profile,
                    message = message,
                    onOpenMarks = { onClearMessage(); nav.navigate(Routes.MARKS) },
                    onOpenAppearance = { nav.navigate(Routes.APPEARANCE) },
                    onSignOut = onSignOut,
                    onDeleteAccount = onDeleteAccount,
                )
            }
            composable(Routes.MARKS) {
                MarksScreen(
                    profile = profile,
                    busy = busy,
                    message = message,
                    onSave = { marks, term -> onSaveMarks(marks, term) { nav.popBackStack() } },
                    onBack = { nav.popBackStack() },
                )
            }
            composable(Routes.APPEARANCE) {
                AppearanceScreen(current = currentTheme, onPick = onThemeChange, onBack = { nav.popBackStack() })
            }
        }
    }
}

private fun NavHostController.goToTab(route: String) {
    navigate(route) {
        popUpTo(Routes.PATH) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
