package za.co.mohlare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import za.co.mohlare.app.data.Provinces
import za.co.mohlare.app.data.UserProfile
import za.co.mohlare.app.ui.components.LanguagePicker
import za.co.mohlare.app.ui.components.MessageBanner
import za.co.mohlare.app.ui.components.PrimaryButton
import za.co.mohlare.app.ui.theme.Mohlare
import za.co.mohlare.app.ui.theme.PatternStrip

/** First-time setup after sign-in: who the learner is, plus POPIA consent. */
@Composable
fun OnboardingScreen(
    suggestedName: String,
    initialLanguage: String,
    busy: Boolean,
    message: String?,
    onSave: (UserProfile) -> Unit,
    onSignOut: () -> Unit,
) {
    val t = Mohlare.tokens
    val c = t.colors
    var name by rememberSaveable { mutableStateOf(suggestedName) }
    var grade by rememberSaveable { mutableIntStateOf(12) }
    var province by rememberSaveable { mutableStateOf("") }
    var language by rememberSaveable { mutableStateOf(initialLanguage) }
    var under18 by rememberSaveable { mutableStateOf(true) }
    var guardianConsent by rememberSaveable { mutableStateOf(false) }
    var privacyAccepted by rememberSaveable { mutableStateOf(false) }
    var provinceMenuOpen by rememberSaveable { mutableStateOf(false) }

    val canSave = name.isNotBlank() && province.isNotBlank() && privacyAccepted && (!under18 || guardianConsent)

    Column(Modifier.fillMaxSize().background(c.background).verticalScroll(rememberScrollState())) {
        PatternStrip()
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Let's set up your plan", color = c.ink, style = MaterialTheme.typography.headlineMedium)
            Text("This takes a minute. You can change it later in Me.", color = c.muted, fontSize = 15.sp)

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Your name") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = c.accent,
                    unfocusedBorderColor = c.border,
                    focusedContainerColor = c.surface,
                    unfocusedContainerColor = c.surface,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Label("Grade")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(11, 12).forEach { g ->
                    val selected = grade == g
                    OutlinedButton(
                        onClick = { grade = g },
                        colors = if (selected) {
                            ButtonDefaults.buttonColors(containerColor = c.ink, contentColor = c.surface)
                        } else {
                            ButtonDefaults.outlinedButtonColors(contentColor = c.ink)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(48.dp),
                    ) { Text("Grade $g") }
                }
            }

            Label("Province")
            Box {
                OutlinedButton(
                    onClick = { provinceMenuOpen = true },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = c.ink),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                ) {
                    Text(province.ifEmpty { "Choose your province" }, modifier = Modifier.weight(1f))
                    Text("▾")
                }
                DropdownMenu(expanded = provinceMenuOpen, onDismissRequest = { provinceMenuOpen = false }) {
                    Provinces.forEach { p ->
                        DropdownMenuItem(
                            text = { Text(p) },
                            onClick = { province = p; provinceMenuOpen = false },
                        )
                    }
                }
            }

            Label("Language")
            LanguagePicker(selected = language, onSelect = { language = it })

            Label("Your privacy")
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("I'm under 18", color = c.ink, fontSize = 15.sp)
                Switch(
                    checked = under18,
                    onCheckedChange = { under18 = it },
                    colors = SwitchDefaults.colors(checkedTrackColor = c.accent),
                )
            }
            if (under18) {
                CheckRow(
                    checked = guardianConsent,
                    onChange = { guardianConsent = it },
                    text = "My parent or guardian knows I'm using Mohlare and agrees.",
                )
            }
            CheckRow(
                checked = privacyAccepted,
                onChange = { privacyAccepted = it },
                text = "I agree that Mohlare can store my name, grade, province and marks to build my plan. I can delete them any time in Me.",
            )
            // TODO: link to the hosted privacy policy once it exists.

            if (message != null) MessageBanner(message)

            PrimaryButton(
                text = "Start my plan",
                busy = busy,
                enabled = canSave,
                onClick = {
                    onSave(
                        UserProfile(
                            name = name,
                            grade = grade,
                            province = province,
                            language = language,
                            under18 = under18,
                            guardianConsent = under18 && guardianConsent,
                            consentAccepted = privacyAccepted,
                        )
                    )
                },
            )
            TextButton(onClick = onSignOut, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Use a different account", color = c.muted)
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(text.uppercase(), color = Mohlare.tokens.colors.muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun CheckRow(checked: Boolean, onChange: (Boolean) -> Unit, text: String) {
    val c = Mohlare.tokens.colors
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(role = Role.Checkbox) { onChange(!checked) },
        verticalAlignment = Alignment.Top,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onChange,
            colors = CheckboxDefaults.colors(checkedColor = c.accent),
        )
        Text(text, color = c.ink, fontSize = 15.sp, modifier = Modifier.padding(top = 12.dp))
    }
}
