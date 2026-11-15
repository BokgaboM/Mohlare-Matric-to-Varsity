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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import za.co.mohlare.app.data.SampleData
import za.co.mohlare.app.ui.components.Pill
import za.co.mohlare.app.ui.components.MohlareCard
import za.co.mohlare.app.ui.theme.PatternStrip
import za.co.mohlare.app.ui.theme.Mohlare

@Composable
fun DiscoverScreen() {
    val t = Mohlare.tokens
    val c = t.colors
    var tab by remember { mutableIntStateOf(0) } // 0 = Courses, 1 = Bursaries
    var query by remember { mutableStateOf("") }

    LazyColumn(
        Modifier.fillMaxSize().background(c.background),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { PatternStrip() }
        item {
            Text(
                "Discover",
                color = c.ink,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
        }
        item {
            // Segmented control: Courses | Bursaries
            Row(
                Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(c.border)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                listOf("Courses", "Bursaries").forEachIndexed { i, label ->
                    val selected = tab == i
                    TextButton(
                        onClick = { tab = i },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (selected) c.surface else c.border),
                    ) {
                        Text(label, color = if (selected) c.ink else c.muted, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium)
                    }
                }
            }
        }
        if (tab == 0) {
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search courses or universities") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = c.accent,
                        unfocusedBorderColor = c.border,
                        focusedContainerColor = c.surface,
                        unfocusedContainerColor = c.surface,
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                )
            }
            item {
                Text(
                    "APS shown the way each university counts it.",
                    color = c.muted,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
            val results = SampleData.courses.filter {
                query.isBlank() || it.name.contains(query, true) || it.university.contains(query, true)
            }
            items(results) { course ->
                Box(Modifier.padding(horizontal = 20.dp)) {
                    MohlareCard {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(course.university, color = c.secondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                            Pill(
                                course.badge,
                                if (course.qualifies) c.successTint else c.warningTint,
                                if (course.qualifies) c.success else c.warning,
                            )
                        }
                        Text(course.name, color = c.ink, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                        Text(course.detail, color = c.muted, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(SampleData.bursaries) { b ->
                Box(Modifier.padding(horizontal = 20.dp)) {
                    MohlareCard(padding = 14.dp) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                            Column(Modifier.weight(1f)) {
                                Text(b.name, color = c.ink, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
                                Text(b.provider, color = c.muted, fontSize = 13.sp)
                            }
                            IconButton(onClick = { /* TODO: save to users/{uid}/saved */ }) {
                                Icon(Icons.Filled.FavoriteBorder, contentDescription = "Save ${b.name}", tint = c.ink)
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            b.covers.forEach { Pill(it, c.secondaryTint, c.secondary) }
                        }
                        HorizontalDivider(color = c.border, modifier = Modifier.padding(vertical = 4.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Closes ${b.closes}", color = if (b.closingSoon) c.accent else c.ink, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text("Checked ${b.checked}", color = c.muted, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
