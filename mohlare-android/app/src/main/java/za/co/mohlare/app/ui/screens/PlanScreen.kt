package za.co.mohlare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import za.co.mohlare.app.data.AppStatus
import za.co.mohlare.app.data.SampleData
import za.co.mohlare.app.ui.components.Pill
import za.co.mohlare.app.ui.components.MohlareCard
import za.co.mohlare.app.ui.theme.PatternStrip
import za.co.mohlare.app.ui.theme.Mohlare

@Composable
fun PlanScreen() {
    val t = Mohlare.tokens
    val c = t.colors
    val ready = remember { mutableStateListOf(*SampleData.documents.map { it.ready }.toTypedArray()) }

    LazyColumn(
        Modifier.fillMaxSize().background(c.background),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { PatternStrip() }
        item {
            Row(
                Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Your plan", color = c.ink, style = MaterialTheme.typography.headlineMedium)
                // TODO: open an "Add application" sheet and save to users/{uid}/applications.
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = c.accent, contentColor = c.onAccent),
                    shape = RoundedCornerShape(50),
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Text("Add", modifier = Modifier.padding(start = 4.dp))
                }
            }
        }
        item {
            Box(Modifier.padding(horizontal = 20.dp)) {
                MohlareCard(padding = 0.dp) {
                    Text("Applications", color = c.ink, fontFamily = t.headingFont, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, modifier = Modifier.padding(start = 14.dp, top = 12.dp, bottom = 4.dp))
                    SampleData.applications.forEach { app ->
                        HorizontalDivider(color = c.border)
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(app.name, color = c.ink, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                                Text(app.closes, color = c.muted, fontSize = 13.sp)
                            }
                            val (bg, fg) = when (app.status) {
                                AppStatus.SUBMITTED -> c.secondaryTint to c.secondary
                                AppStatus.NEEDS_DOCUMENTS -> c.warningTint to c.warning
                                AppStatus.PLANNING -> c.tint to c.ink
                                AppStatus.ACCEPTED -> c.successTint to c.success
                            }
                            Pill(app.status.label, bg, fg)
                        }
                    }
                }
            }
        }
        item {
            Box(Modifier.padding(horizontal = 20.dp)) {
                MohlareCard {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Documents", color = c.ink, fontFamily = t.headingFont, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                        Text("${ready.count { it }} of ${ready.size} ready", color = c.muted, fontSize = 13.sp)
                    }
                    SampleData.documents.forEachIndexed { i, doc ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = ready[i],
                                onCheckedChange = { ready[i] = it },
                                colors = CheckboxDefaults.colors(checkedColor = c.accent),
                            )
                            Text(doc.name, color = c.ink, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}
