package za.co.mohlare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.foundation.Canvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import za.co.mohlare.app.ui.theme.AllThemes
import za.co.mohlare.app.ui.theme.PatternStrip
import za.co.mohlare.app.ui.theme.Mohlare
import za.co.mohlare.app.ui.theme.MohlareThemeId
import za.co.mohlare.app.ui.theme.drawThemePattern

@Composable
fun AppearanceScreen(current: MohlareThemeId, onPick: (MohlareThemeId) -> Unit, onBack: () -> Unit) {
    val t = Mohlare.tokens
    val c = t.colors
    Column(
        Modifier.fillMaxSize().background(c.background).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PatternStrip()
        Row(Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = c.ink)
            }
            Text("Appearance", color = c.ink, style = MaterialTheme.typography.headlineMedium)
        }
        Text(
            "CHOOSE YOUR LOOK",
            color = c.muted,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        Row(
            Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AllThemes.forEach { theme ->
                val selected = theme.id == current
                Column(
                    Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(theme.colors.surface)
                        .border(
                            width = if (selected) 2.5.dp else 1.dp,
                            color = if (selected) c.accent else c.border,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .clickable(role = Role.RadioButton) { onPick(theme.id) }
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Canvas(
                        Modifier.fillMaxWidth().height(72.dp).clip(RoundedCornerShape(10.dp))
                    ) { drawThemePattern(theme, tile = 30.dp.toPx(), step = 8.dp.toPx()) }
                    Text(theme.displayName, color = theme.colors.ink, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                }
            }
        }
        Text(
            "New looks drop each season. They arrive without an app update.",
            color = c.muted,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        // TODO: dark versions of each theme, following the phone's setting.
    }
}
