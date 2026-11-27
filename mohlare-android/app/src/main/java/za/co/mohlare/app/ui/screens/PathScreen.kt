package za.co.mohlare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import za.co.mohlare.app.data.SampleData
import za.co.mohlare.app.data.UserProfile
import za.co.mohlare.app.ui.components.DateBlock
import za.co.mohlare.app.ui.components.Pill
import za.co.mohlare.app.ui.components.SectionHeader
import za.co.mohlare.app.ui.components.MohlareCard
import za.co.mohlare.app.ui.theme.HeroStyle
import za.co.mohlare.app.ui.theme.PatternBlock
import za.co.mohlare.app.ui.theme.PatternStrip
import za.co.mohlare.app.ui.theme.Mohlare

@Composable
fun PathScreen(profile: UserProfile, onUpdateMarks: () -> Unit, onSeePlan: () -> Unit) {
    val t = Mohlare.tokens
    val c = t.colors
    LazyColumn(
        Modifier.fillMaxSize().background(c.background),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { PatternStrip() }
        item {
            Column(Modifier.padding(horizontal = 20.dp)) {
                Text("Dumela, ${profile.firstName}", color = c.ink, style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
                Text(profile.gradeAndProvince, color = c.muted, fontSize = 14.sp)
            }
        }
        item { Box(Modifier.padding(horizontal = 20.dp)) { ApsHero(onUpdateMarks) } }
        item { Box(Modifier.padding(horizontal = 20.dp)) { SectionHeader("Next up", "See plan", onSeePlan) } }
        items(SampleData.deadlines) { d ->
            Box(Modifier.padding(horizontal = 20.dp)) {
                MohlareCard(padding = 12.dp) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        DateBlock(d.day, d.month, highlighted = d.urgent)
                        Column(Modifier.weight(1f)) {
                            Text(d.title, color = c.ink, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                            Text(d.subtitle, color = c.muted, fontSize = 13.sp)
                        }
                        Pill(d.dueLabel, if (d.urgent) c.warningTint else c.tint, if (d.urgent) c.warning else c.ink)
                    }
                }
            }
        }
    }
}

/** The APS block. Heritage draws it as a dark card; Litema as an arch. */
@Composable
private fun ApsHero(onUpdateMarks: () -> Unit) {
    val t = Mohlare.tokens
    val c = t.colors
    when (t.hero) {
        HeroStyle.CARD -> Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(t.cardRadius))
                .background(c.hero)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text("YOUR APS", color = c.heroLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Text("${SampleData.aps}", color = c.onHero, fontFamily = t.headingFont, fontWeight = FontWeight.Bold, fontSize = 56.sp)
            Text("From ${SampleData.subjectCount} subjects · ${SampleData.matchingCourses} saved courses match", color = c.onHero.copy(alpha = 0.85f), fontSize = 13.sp)
            OutlinedButton(
                onClick = onUpdateMarks,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = c.onHero),
                shape = RoundedCornerShape(50),
            ) { Text("Update marks") }
        }
        HeroStyle.ARCH -> Box(
            Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(t.cardRadius)),
            contentAlignment = Alignment.BottomCenter,
        ) {
            PatternBlock(Modifier.fillMaxSize())
            Column(
                Modifier
                    .width(200.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 100.dp, topEnd = 100.dp))
                    .background(c.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                Text("${SampleData.aps}", color = c.ink, fontFamily = t.headingFont, fontWeight = FontWeight.Bold, fontSize = 56.sp)
                Text("YOUR APS", color = c.muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                androidx.compose.material3.TextButton(onClick = onUpdateMarks) {
                    Text("Update marks", color = c.accent, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
