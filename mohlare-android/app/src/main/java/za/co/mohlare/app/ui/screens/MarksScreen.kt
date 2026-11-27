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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import za.co.mohlare.app.data.Aps
import za.co.mohlare.app.data.NscSubjects
import za.co.mohlare.app.data.ResultTerms
import za.co.mohlare.app.data.SubjectMark
import za.co.mohlare.app.data.UserProfile
import za.co.mohlare.app.ui.components.MessageBanner
import za.co.mohlare.app.ui.components.MohlareCard
import za.co.mohlare.app.ui.components.Pill
import za.co.mohlare.app.ui.components.PrimaryButton
import za.co.mohlare.app.ui.theme.Mohlare
import za.co.mohlare.app.ui.theme.PatternStrip

private data class MarkRow(val subject: String, val percent: String)

private const val MAX_SUBJECTS = 9

/** Enter subjects and marks; the APS updates as you type. */
@Composable
fun MarksScreen(
    profile: UserProfile,
    busy: Boolean,
    message: String?,
    onSave: (marks: List<SubjectMark>, term: String) -> Unit,
    onBack: () -> Unit,
) {
    val t = Mohlare.tokens
    val c = t.colors

    val rows = remember {
        mutableStateListOf<MarkRow>().apply {
            if (profile.hasMarks) {
                profile.marks.forEach { add(MarkRow(it.subject, it.percent.toString())) }
            } else {
                // A typical NSC subject set: 7 subjects, including Life Orientation.
                listOf("", "", "", Aps.LIFE_ORIENTATION, "", "", "").forEach { add(MarkRow(it, "")) }
            }
        }
    }
    var term by remember {
        mutableStateOf(profile.marksTerm.ifBlank {
            if (profile.grade == 11) ResultTerms.first() else ResultTerms[2]
        })
    }
    var termMenuOpen by remember { mutableStateOf(false) }

    // Only rows with a subject and a valid mark count towards the APS.
    val valid = rows.mapNotNull { r ->
        val p = r.percent.toIntOrNull()
        if (r.subject.isNotBlank() && p != null && p in 0..100) SubjectMark(r.subject, p) else null
    }
    val result = Aps.standard(valid)
    val chosen = rows.map { it.subject }.filter { it.isNotBlank() }
    val hasDuplicates = chosen.size != chosen.toSet().size
    val hasBadMark = rows.any { it.percent.isNotBlank() && (it.percent.toIntOrNull() ?: -1) !in 0..100 }
    val canSave = result.isComplete && !hasDuplicates && !hasBadMark

    LazyColumn(
        Modifier.fillMaxSize().background(c.background),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { PatternStrip() }
        item {
            Row(Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = c.ink)
                }
                Text("My marks", color = c.ink, style = MaterialTheme.typography.headlineMedium)
            }
        }

        // Live APS
        item {
            Box(Modifier.padding(horizontal = 20.dp)) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(c.hero, RoundedCornerShape(t.cardRadius))
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text("YOUR APS", color = c.heroLabel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text(
                        "${result.total}",
                        color = c.onHero,
                        fontFamily = t.headingFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 48.sp,
                    )
                    Text(
                        if (result.isComplete) {
                            "Best 6 subjects. Life Orientation is not counted."
                        } else {
                            "Add marks for ${6 - result.counted.size} more subject(s) to get your full APS."
                        },
                        color = c.onHero.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                    )
                }
            }
        }

        // Which results
        item {
            Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("THESE MARKS ARE FROM", color = c.muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Box {
                    OutlinedButton(
                        onClick = { termMenuOpen = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = c.ink),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                    ) {
                        Text(term, modifier = Modifier.weight(1f))
                        Text("▾")
                    }
                    DropdownMenu(expanded = termMenuOpen, onDismissRequest = { termMenuOpen = false }) {
                        ResultTerms.forEach { option ->
                            DropdownMenuItem(text = { Text(option) }, onClick = { term = option; termMenuOpen = false })
                        }
                    }
                }
            }
        }

        // Subject rows
        itemsIndexed(rows) { index, row ->
            Box(Modifier.padding(horizontal = 20.dp)) {
                SubjectRow(
                    row = row,
                    taken = chosen.toSet() - row.subject,
                    canRemove = rows.size > 6,
                    onChange = { rows[index] = it },
                    onRemove = { rows.removeAt(index) },
                )
            }
        }

        item {
            Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (rows.size < MAX_SUBJECTS) {
                    TextButton(onClick = { rows.add(MarkRow("", "")) }) {
                        Text("+ Add a subject", color = c.accent, fontWeight = FontWeight.SemiBold)
                    }
                }
                if (hasDuplicates) MessageBanner("You've picked the same subject twice.")
                if (hasBadMark) MessageBanner("Marks must be between 0 and 100.")
                if (message != null) MessageBanner(message)
                PrimaryButton(
                    text = "Save marks",
                    busy = busy,
                    enabled = canSave,
                    onClick = { onSave(valid, term) },
                )
                Text(
                    "This is the standard APS used by UJ, UP and many others. Wits, UCT and some universities " +
                        "count differently; Mohlare will show each university's own score on its courses.",
                    color = c.muted,
                    fontSize = 13.sp,
                )
            }
        }
    }
}

@Composable
private fun SubjectRow(
    row: MarkRow,
    taken: Set<String>,
    canRemove: Boolean,
    onChange: (MarkRow) -> Unit,
    onRemove: () -> Unit,
) {
    val c = Mohlare.tokens.colors
    var menuOpen by remember { mutableStateOf(false) }
    val percent = row.percent.toIntOrNull()
    val isLo = Aps.isLifeOrientation(row.subject)

    MohlareCard(padding = 10.dp) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.weight(1f)) {
                TextButton(onClick = { menuOpen = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        row.subject.ifBlank { "Choose subject" },
                        color = if (row.subject.isBlank()) c.muted else c.ink,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    NscSubjects.filter { it !in taken }.forEach { subject ->
                        DropdownMenuItem(
                            text = { Text(subject) },
                            onClick = { onChange(row.copy(subject = subject)); menuOpen = false },
                        )
                    }
                }
            }
            OutlinedTextField(
                value = row.percent,
                onValueChange = { new -> if (new.length <= 3 && new.all { it.isDigit() }) onChange(row.copy(percent = new)) },
                label = { Text("%") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = c.accent,
                    unfocusedBorderColor = c.border,
                ),
                modifier = Modifier.width(84.dp),
            )
            if (canRemove) {
                IconButton(onClick = onRemove) {
                    Icon(Icons.Filled.Close, contentDescription = "Remove ${row.subject.ifBlank { "subject" }}", tint = c.muted)
                }
            }
        }
        if (percent != null && percent in 0..100 && row.subject.isNotBlank()) {
            if (isLo) {
                Pill("Not counted in APS", c.tint, c.muted)
            } else {
                Pill("Level ${Aps.levelFor(percent)}", c.secondaryTint, c.secondary)
            }
        }
    }
}
