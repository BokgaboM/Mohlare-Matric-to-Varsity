package za.co.mohlare.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val LocalMohlare = staticCompositionLocalOf { HeritageTokens }

/** Read the current theme anywhere: `Mohlare.tokens.colors.accent`. */
object Mohlare {
    val tokens: MohlareTokens
        @Composable @ReadOnlyComposable get() = LocalMohlare.current
}

@Composable
fun MohlareTheme(themeId: MohlareThemeId, content: @Composable () -> Unit) {
    val t = tokensFor(themeId)
    val c = t.colors
    val scheme = lightColorScheme(
        primary = c.accent,
        onPrimary = c.onAccent,
        secondary = c.secondary,
        background = c.background,
        onBackground = c.ink,
        surface = c.surface,
        onSurface = c.ink,
        onSurfaceVariant = c.muted,
        outline = c.border,
    )
    val head = t.headingFont
    val body = t.bodyFont
    val typography = Typography(
        headlineLarge = TextStyle(fontFamily = head, fontWeight = FontWeight.Bold, fontSize = 30.sp),
        headlineMedium = TextStyle(fontFamily = head, fontWeight = FontWeight.Bold, fontSize = 26.sp),
        titleLarge = TextStyle(fontFamily = head, fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
        titleMedium = TextStyle(fontFamily = body, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
        bodyLarge = TextStyle(fontFamily = body, fontSize = 16.sp),
        bodyMedium = TextStyle(fontFamily = body, fontSize = 14.sp),
        labelLarge = TextStyle(fontFamily = body, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
        labelMedium = TextStyle(fontFamily = body, fontWeight = FontWeight.SemiBold, fontSize = 12.sp),
    )
    CompositionLocalProvider(LocalMohlare provides t) {
        MaterialTheme(colorScheme = scheme, typography = typography, content = content)
    }
}
