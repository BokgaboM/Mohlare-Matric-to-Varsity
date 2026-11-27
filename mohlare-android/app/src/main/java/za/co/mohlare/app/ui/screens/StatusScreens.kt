package za.co.mohlare.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import za.co.mohlare.app.R
import za.co.mohlare.app.ui.theme.Mohlare

/** Shown for a moment while Mohlare checks whether you're signed in. */
@Composable
fun LoadingScreen() {
    val c = Mohlare.tokens.colors
    Column(
        Modifier.fillMaxSize().background(c.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(painterResource(R.drawable.logo_mark), contentDescription = "Mohlare", modifier = Modifier.size(96.dp))
        CircularProgressIndicator(color = c.accent, modifier = Modifier.padding(top = 24.dp))
    }
}

/** Shown if the profile couldn't load, e.g. no internet on first open. */
@Composable
fun FailedScreen(message: String, onSignOut: () -> Unit) {
    val c = Mohlare.tokens.colors
    Column(
        Modifier.fillMaxSize().background(c.background).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        Image(painterResource(R.drawable.logo_mark), contentDescription = null, modifier = Modifier.size(80.dp))
        Text(message, color = c.ink, fontSize = 16.sp, textAlign = TextAlign.Center)
        Text("Close Mohlare and open it again to retry.", color = c.muted, fontSize = 14.sp, textAlign = TextAlign.Center)
        TextButton(onClick = onSignOut) { Text("Sign out", color = c.accent) }
    }
}
