package za.co.mohlare.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import za.co.mohlare.app.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import za.co.mohlare.app.ui.theme.HeroStyle
import za.co.mohlare.app.ui.theme.PatternBlock
import za.co.mohlare.app.ui.components.LanguagePicker
import za.co.mohlare.app.ui.components.MessageBanner
import za.co.mohlare.app.ui.components.PrimaryButton
import za.co.mohlare.app.ui.theme.Mohlare

@Composable
fun WelcomeScreen(
    language: String,
    onLanguageChange: (String) -> Unit,
    busy: Boolean,
    message: String?,
    onGoogle: () -> Unit,
    onEmail: () -> Unit,
) {
    val t = Mohlare.tokens
    val c = t.colors

    Column(
        Modifier
            .fillMaxSize()
            .background(c.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Hero: a pattern block, shaped by the theme.
        val heroShape = when (t.hero) {
            HeroStyle.CARD -> RoundedCornerShape(0.dp)
            HeroStyle.ARCH -> RoundedCornerShape(topStart = 170.dp, topEnd = 170.dp, bottomStart = 20.dp, bottomEnd = 20.dp)
        }
        val heroPadding = if (t.hero == HeroStyle.ARCH) 28.dp else 0.dp
        Box(
            Modifier
                .padding(horizontal = heroPadding, vertical = if (t.hero == HeroStyle.ARCH) 24.dp else 0.dp)
                .fillMaxWidth()
                .height(300.dp)
                .clip(heroShape)
        ) {
            PatternBlock(Modifier.fillMaxSize())
        }

        Column(
            Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.logo_mark),
                contentDescription = "Mohlare logo",
                modifier = Modifier.size(88.dp),
            )
            Column {
                Text("Mohlare", color = c.ink, fontFamily = t.headingFont, fontWeight = FontWeight.Bold, fontSize = 44.sp)
                Text("MATRIC TO VARSITY", color = c.accent, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 3.sp)
            }
            Text(
                "Your way to varsity, one step at a time. Marks, courses, bursaries and deadlines in one plan.",
                color = c.muted,
                fontSize = 16.sp,
            )

            Text("CHOOSE YOUR LANGUAGE", color = c.muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            LanguagePicker(selected = language, onSelect = onLanguageChange)

            Spacer(Modifier.height(4.dp))
            if (message != null) MessageBanner(message)
            PrimaryButton("Continue with Google", busy = busy, onClick = onGoogle)
            OutlinedButton(
                onClick = onEmail,
                enabled = !busy,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = c.ink),
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) { Text("Use email instead", fontSize = 16.sp, fontWeight = FontWeight.SemiBold) }
            Text("Free for Grade 11 and 12 learners.", color = c.muted, fontSize = 13.sp)
        }
    }
}
