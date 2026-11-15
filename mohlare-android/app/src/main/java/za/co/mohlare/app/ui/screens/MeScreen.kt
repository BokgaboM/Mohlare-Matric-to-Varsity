package za.co.mohlare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import za.co.mohlare.app.data.SampleData
import za.co.mohlare.app.ui.components.MohlareCard
import za.co.mohlare.app.ui.theme.PatternStrip
import za.co.mohlare.app.ui.theme.Mohlare

@Composable
fun MeScreen(onOpenAppearance: () -> Unit) {
    val t = Mohlare.tokens
    val c = t.colors
    LazyColumn(
        Modifier.fillMaxSize().background(c.background),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { PatternStrip() }
        item {
            Row(
                Modifier.padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Box(
                    Modifier
                        .size(64.dp)
                        .border(3.dp, c.patternMain, CircleShape)
                        .padding(5.dp)
                        .background(c.hero, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("LM", color = c.onHero, fontFamily = t.headingFont, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Column {
                    Text(SampleData.learnerFullName, color = c.ink, style = MaterialTheme.typography.headlineMedium)
                    Text(SampleData.gradeAndProvince, color = c.muted, fontSize = 14.sp)
                }
            }
        }
        item {
            Section("Me & my look") {
                SettingRow("${SampleData.subjectCount} subjects · APS ${SampleData.aps}", "Edit") { /* TODO: marks editor */ }
                SettingRow("Appearance", t.displayName, onClick = onOpenAppearance)
            }
        }
        item {
            Section("Reminders") {
                ToggleRow("7 days before a deadline", true)
                ToggleRow("1 day before a deadline", true)
                ToggleRow("New bursaries for my field", false)
            }
        }
        item {
            Section("Privacy") {
                SettingRow("Privacy policy", "") { }
                SettingRow("Download my data", "") { }
                // Required by Google Play and POPIA: let learners delete their account and data.
                SettingRow("Delete my account and data", "", color = c.accent) { }
            }
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    val t = Mohlare.tokens
    Box(Modifier.padding(horizontal = 20.dp)) {
        MohlareCard(padding = 0.dp) {
            Text(title, color = t.colors.ink, fontFamily = t.headingFont, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, modifier = Modifier.padding(start = 14.dp, top = 12.dp, bottom = 4.dp))
            content()
        }
    }
}

@Composable
private fun SettingRow(label: String, value: String, color: Color? = null, onClick: () -> Unit) {
    val c = Mohlare.tokens.colors
    HorizontalDivider(color = c.border)
    Row(
        Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = color ?: c.ink, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        Text(value, color = c.muted, fontSize = 14.sp)
    }
}

@Composable
private fun ToggleRow(label: String, initial: Boolean) {
    val c = Mohlare.tokens.colors
    var on by remember { mutableStateOf(initial) }
    HorizontalDivider(color = c.border)
    Row(
        Modifier.fillMaxWidth().heightIn(min = 48.dp).padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = c.ink, fontSize = 15.sp)
        Switch(
            checked = on,
            onCheckedChange = { on = it },
            colors = SwitchDefaults.colors(checkedTrackColor = c.accent),
        )
    }
}
