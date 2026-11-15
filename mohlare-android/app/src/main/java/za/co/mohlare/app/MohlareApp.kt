package za.co.mohlare.app

import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
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
    const val WELCOME = "welcome"
    const val PATH = "path"
    const val DISCOVER = "discover"
    const val PLAN = "plan"
    const val ME = "me"
    const val APPEARANCE = "appearance"
}

private enum class Tab(val route: String, val label: String, val icon: ImageVector) {
    PATH(Routes.PATH, "Path", Icons.Filled.Home),
    DISCOVER(Routes.DISCOVER, "Discover", Icons.Filled.Search),
    PLAN(Routes.PLAN, "Plan", Icons.Filled.DateRange),
    ME(Routes.ME, "Me", Icons.Filled.Person),
}

@Composable
fun MohlareApp(currentTheme: MohlareThemeId, onThemeChange: (MohlareThemeId) -> Unit) {
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
        NavHost(nav, startDestination = Routes.WELCOME, modifier = Modifier.padding(padding)) {
            composable(Routes.WELCOME) {
                WelcomeScreen(onContinue = {
                    nav.navigate(Routes.PATH) { popUpTo(Routes.WELCOME) { inclusive = true } }
                })
            }
            composable(Routes.PATH) {
                PathScreen(
                    onUpdateMarks = { nav.goToTab(Routes.ME) },
                    onSeePlan = { nav.goToTab(Routes.PLAN) },
                )
            }
            composable(Routes.DISCOVER) { DiscoverScreen() }
            composable(Routes.PLAN) { PlanScreen() }
            composable(Routes.ME) { MeScreen(onOpenAppearance = { nav.navigate(Routes.APPEARANCE) }) }
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
