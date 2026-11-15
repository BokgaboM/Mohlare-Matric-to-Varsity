package za.co.mohlare.app.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** The thin pattern band at the top of each screen, drawn in the current theme's style. */
@Composable
fun PatternStrip(modifier: Modifier = Modifier, height: Dp = 20.dp) {
    val t = Mohlare.tokens
    Canvas(modifier.fillMaxWidth().height(height)) {
        drawThemePattern(t, tile = 26.dp.toPx(), step = 6.dp.toPx())
    }
}

/** A larger block of pattern, used on the welcome screen. */
@Composable
fun PatternBlock(modifier: Modifier = Modifier) {
    val t = Mohlare.tokens
    Canvas(modifier) {
        drawThemePattern(t, tile = 64.dp.toPx(), step = 16.dp.toPx())
    }
}

fun DrawScope.drawThemePattern(t: MohlareTokens, tile: Float, step: Float) {
    val c = t.colors
    when (t.pattern) {
        PatternType.TRIANGLES -> drawTriangleBand(c.patternBase, c.patternMain, c.patternDetail, tile)
        PatternType.ARCS -> drawLitemaArcs(c.patternBase, c.patternMain, step, 1.4f * density)
    }
}

/** Ndebele-inspired row of nested triangles. */
fun DrawScope.drawTriangleBand(base: Color, outer: Color, inner: Color, tile: Float) {
    drawRect(base)
    val h = size.height
    var x = 0f
    while (x < size.width) {
        val outerPath = Path().apply {
            moveTo(x, h); lineTo(x + tile / 2f, h * 0.15f); lineTo(x + tile, h); close()
        }
        drawPath(outerPath, outer)
        val innerPath = Path().apply {
            moveTo(x + tile * 0.23f, h); lineTo(x + tile / 2f, h * 0.55f); lineTo(x + tile * 0.77f, h); close()
        }
        drawPath(innerPath, inner)
        x += tile
    }
}

/** Litema-inspired nested arches, like lines drawn in a mural with fingers. */
fun DrawScope.drawLitemaArcs(base: Color, line: Color, step: Float, strokeWidth: Float) {
    drawRect(base)
    val h = size.height
    val w = size.width
    var i = 0
    while (i * step < h) {
        val controlY = h - 2f * (h - i * step)
        val arc = Path().apply {
            moveTo(-20f, h)
            quadraticTo(w / 2f, controlY, w + 20f, h)
        }
        drawPath(arc, line, style = Stroke(width = strokeWidth))
        i++
    }
}
