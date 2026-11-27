package za.co.mohlare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import za.co.mohlare.app.ui.components.MessageBanner
import za.co.mohlare.app.ui.components.PrimaryButton
import za.co.mohlare.app.ui.theme.Mohlare
import za.co.mohlare.app.ui.theme.PatternStrip

@Composable
fun EmailAuthScreen(
    busy: Boolean,
    message: String?,
    onSignIn: (email: String, password: String) -> Unit,
    onRegister: (email: String, password: String) -> Unit,
    onForgotPassword: (email: String) -> Unit,
    onBack: () -> Unit,
) {
    val t = Mohlare.tokens
    val c = t.colors
    var registering by rememberSaveable { mutableStateOf(true) }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    val emailOk = email.contains('@') && email.contains('.')
    val passwordOk = password.length >= 6
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = c.accent,
        unfocusedBorderColor = c.border,
        focusedContainerColor = c.surface,
        unfocusedContainerColor = c.surface,
    )

    Column(
        Modifier.fillMaxSize().background(c.background).verticalScroll(rememberScrollState()),
    ) {
        PatternStrip()
        Row(Modifier.padding(horizontal = 8.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = c.ink)
            }
            Text(
                if (registering) "Create your account" else "Welcome back",
                color = c.ink,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        Column(
            Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(14.dp),
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                supportingText = { if (registering) Text("At least 6 characters") },
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    TextButton(onClick = { showPassword = !showPassword }) {
                        Text(if (showPassword) "Hide" else "Show", color = c.accent)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth(),
            )

            if (message != null) MessageBanner(message)

            PrimaryButton(
                text = if (registering) "Create account" else "Sign in",
                busy = busy,
                enabled = emailOk && passwordOk,
                onClick = { if (registering) onRegister(email, password) else onSignIn(email, password) },
            )

            if (!registering) {
                TextButton(
                    onClick = { onForgotPassword(email) },
                    enabled = emailOk && !busy,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) { Text("Forgot password? Type your email, then tap here", color = c.accent, fontSize = 14.sp) }
            }

            TextButton(
                onClick = { registering = !registering },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Text(
                    if (registering) "Already have an account? Sign in" else "New here? Create an account",
                    color = c.ink,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
