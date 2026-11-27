package za.co.mohlare.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import za.co.mohlare.app.ui.theme.Mohlare

/** A rounded surface card using the theme's colours and corner radius. */
@Composable
fun MohlareCard(
    modifier: Modifier = Modifier,
    padding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val t = Mohlare.tokens
    val shape = RoundedCornerShape(t.cardRadius)
    Column(
        modifier
            .fillMaxWidth()
            .background(t.colors.surface, shape)
            .border(1.dp, t.colors.border, shape)
            .padding(padding),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        content = content,
    )
}

/** A small rounded label, e.g. "You qualify" or "Submitted". */
@Composable
fun Pill(text: String, background: Color, foreground: Color, modifier: Modifier = Modifier) {
    Box(
        modifier
            .background(background, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, color = foreground, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

/** Square date block used in deadline lists. */
@Composable
fun DateBlock(day: String, month: String, highlighted: Boolean) {
    val c = Mohlare.tokens.colors
    Column(
        Modifier
            .size(48.dp)
            .background(if (highlighted) c.accent else c.tint, RoundedCornerShape(12.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            day,
            color = if (highlighted) c.onAccent else c.ink,
            fontFamily = Mohlare.tokens.headingFont,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
        Text(
            month,
            color = if (highlighted) c.onAccent else c.ink,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
    }
}

/** Section heading with an optional action on the right. */
@Composable
fun SectionHeader(title: String, action: String? = null, onAction: () -> Unit = {}) {
    val t = Mohlare.tokens
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, color = t.colors.ink, fontFamily = t.headingFont, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
        if (action != null) {
            androidx.compose.material3.TextButton(onClick = onAction) {
                Text(action, color = t.colors.accent, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

/** The four language buttons, two per row. */
@Composable
fun LanguagePicker(selected: String, onSelect: (String) -> Unit) {
    val c = Mohlare.tokens.colors
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        za.co.mohlare.app.data.Languages.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { lang ->
                    val isSelected = lang == selected
                    androidx.compose.material3.OutlinedButton(
                        onClick = { onSelect(lang) },
                        colors = if (isSelected) {
                            androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = c.ink, contentColor = c.surface)
                        } else {
                            androidx.compose.material3.ButtonDefaults.outlinedButtonColors(contentColor = c.ink)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(48.dp),
                    ) { Text(lang) }
                }
            }
        }
    }
}

/** The main action button. Shows a spinner and ignores taps while [busy]. */
@Composable
fun PrimaryButton(text: String, busy: Boolean, enabled: Boolean = true, onClick: () -> Unit) {
    val c = Mohlare.tokens.colors
    androidx.compose.material3.Button(
        onClick = onClick,
        enabled = enabled && !busy,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = c.accent, contentColor = c.onAccent),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth().height(52.dp),
    ) {
        if (busy) {
            androidx.compose.material3.CircularProgressIndicator(
                color = c.onAccent,
                strokeWidth = 2.dp,
                modifier = Modifier.size(22.dp),
            )
        } else {
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

/** A short message for the learner, e.g. "That email or password isn't right." */
@Composable
fun MessageBanner(text: String) {
    val c = Mohlare.tokens.colors
    Text(
        text,
        color = c.warning,
        fontSize = 14.sp,
        modifier = Modifier
            .fillMaxWidth()
            .background(c.warningTint, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
    )
}
