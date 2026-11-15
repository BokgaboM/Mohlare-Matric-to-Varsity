package za.co.mohlare.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Every theme Mohlare knows about. Add a new look by adding an id and a [MohlareTokens] below. */
enum class MohlareThemeId { HERITAGE, LITEMA }

/** Which pattern drawer a theme uses. New pattern types need an app update; new colours do not. */
enum class PatternType { TRIANGLES, ARCS }

/** How the APS hero on the home screen is shaped. */
enum class HeroStyle { CARD, ARCH }

data class MohlareColors(
    val background: Color,
    val surface: Color,
    val ink: Color,
    val muted: Color,
    val accent: Color,
    val onAccent: Color,
    val border: Color,
    val tint: Color,
    val hero: Color,
    val onHero: Color,
    val heroLabel: Color,
    val secondary: Color,
    val secondaryTint: Color,
    val success: Color,
    val successTint: Color,
    val warning: Color,
    val warningTint: Color,
    val patternBase: Color,
    val patternMain: Color,
    val patternDetail: Color,
)

data class MohlareTokens(
    val id: MohlareThemeId,
    val displayName: String,
    val colors: MohlareColors,
    /** TODO: replace with the real font files (see README, step "Fonts"). */
    val headingFont: FontFamily,
    val bodyFont: FontFamily,
    val cardRadius: Dp,
    val pattern: PatternType,
    val hero: HeroStyle,
)

val HeritageTokens = MohlareTokens(
    id = MohlareThemeId.HERITAGE,
    displayName = "Heritage",
    colors = MohlareColors(
        background = Color(0xFFF5EDE2),
        surface = Color(0xFFFFFFFF),
        ink = Color(0xFF2A1D14),
        muted = Color(0xFF6B5647),
        accent = Color(0xFFA8432A),
        onAccent = Color(0xFFFFFFFF),
        border = Color(0xFFEADCCB),
        tint = Color(0xFFF3E2CF),
        hero = Color(0xFF2A1D14),
        onHero = Color(0xFFFFFFFF),
        heroLabel = Color(0xFFE8B85A),
        secondary = Color(0xFF283593),
        secondaryTint = Color(0xFFE4E6F4),
        success = Color(0xFF2F4A2C),
        successTint = Color(0xFFDDE8D6),
        warning = Color(0xFF6E4A07),
        warningTint = Color(0xFFF6E2B8),
        patternBase = Color(0xFF2A1D14),
        patternMain = Color(0xFFD9A441),
        patternDetail = Color(0xFFA8432A),
    ),
    headingFont = FontFamily.SansSerif,
    bodyFont = FontFamily.SansSerif,
    cardRadius = 18.dp,
    pattern = PatternType.TRIANGLES,
    hero = HeroStyle.CARD,
)

val LitemaTokens = MohlareTokens(
    id = MohlareThemeId.LITEMA,
    displayName = "Litema",
    colors = MohlareColors(
        background = Color(0xFFEBDDC8),
        surface = Color(0xFFF6EDE0),
        ink = Color(0xFF3B2416),
        muted = Color(0xFF6A5240),
        accent = Color(0xFF8A3B1E),
        onAccent = Color(0xFFFFFFFF),
        border = Color(0xFFD8C4A6),
        tint = Color(0xFFE3CFB3),
        hero = Color(0xFF3B2416),
        onHero = Color(0xFFF6EDE0),
        heroLabel = Color(0xFFE3B567),
        secondary = Color(0xFF46502A),
        secondaryTint = Color(0xFFE0E3CC),
        success = Color(0xFF2F4A2C),
        successTint = Color(0xFFDDE8D6),
        warning = Color(0xFF6E4A07),
        warningTint = Color(0xFFF6E2B8),
        patternBase = Color(0xFF3B2416),
        patternMain = Color(0xFF8A6A48),
        patternDetail = Color(0xFFD49A3A),
    ),
    headingFont = FontFamily.Serif,
    bodyFont = FontFamily.SansSerif,
    cardRadius = 24.dp,
    pattern = PatternType.ARCS,
    hero = HeroStyle.ARCH,
)

fun tokensFor(id: MohlareThemeId): MohlareTokens = when (id) {
    MohlareThemeId.HERITAGE -> HeritageTokens
    MohlareThemeId.LITEMA -> LitemaTokens
}

val AllThemes = listOf(HeritageTokens, LitemaTokens)
