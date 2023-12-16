package runner.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import formattedTime
import runner.ResultState

@Composable
fun TextValue(text: String, color: Color = MaterialTheme.colors.onBackground) =
    Box(modifier = Modifier.border(1.dp, Color.Gray), contentAlignment = Alignment.Center) {
        Text(text, modifier = Modifier.padding(4.dp), color = color, fontFamily = FontFamily.Monospace)
    }

@Composable
fun ResultIcon(result: ResultState?) {
    Spacer(Modifier.width(2.dp))
    when (result?.correct) {
        true -> Icon(Icons.Default.Check, "", tint = Color.Green)
        false -> Icon(Icons.Default.Close, "", tint = Color.Red)
        else -> Icon(Icons.Default.Build, "")
    }
}

@Composable
fun Label(text: String, color: Color = MaterialTheme.colors.onBackground, minWidth: Dp = Dp.Unspecified) =
    Text(text = text, modifier = Modifier.padding(4.dp).widthIn(min = minWidth), color = color)

@Composable
fun TimeValue(time: Double, colorRange: List<Double> = listOf(0.1, 0.5, 1.0)) {
    val posTime = time.coerceAtLeast(0.0)
    TextValue(posTime.formattedTime(), color = colorRange.getColor(posTime))
}

private fun List<Double>.getColor(value: Double) = when {
    value < this[0] -> Color.Green
    value < this[1] -> Color.Yellow
    value < this[2] -> Color(252, 145, 5)
    else -> Color.Red
}

@Composable
fun BorderLeftShape(thickness: Dp) : Shape {
    val thicknessPx = with(LocalDensity.current) {thickness.toPx()}
    return GenericShape { size, _ ->
        moveTo(0F, 0F)
        lineTo(0F, size.height)
        lineTo(thicknessPx, size.height)
        lineTo(thicknessPx, 0F)
    }
}

@Composable
fun BorderRightShape(thickness: Dp) : Shape {
    val thicknessPx = with(LocalDensity.current) {thickness.toPx()}
    return GenericShape { size, _ ->
        moveTo(size.width - thicknessPx, 0F)
        lineTo(size.width - thicknessPx, size.height)
        lineTo(size.width, size.height)
        lineTo(size.width, 0F)
    }
}

@Composable
fun BorderTopShape(thickness: Dp) : Shape {
    val thicknessPx = with(LocalDensity.current) {thickness.toPx()}
    return GenericShape { size, _ ->
        moveTo(0F, 0F)
        lineTo(size.width, 0F)
        lineTo(size.width, thicknessPx)
        lineTo(0F, thicknessPx)
    }
}

@Composable
fun BorderBottomShape(thickness: Dp) : Shape {
    val thicknessPx = with(LocalDensity.current) {thickness.toPx()}
    return GenericShape { size, _ ->
        moveTo(0F, size.height)
        lineTo(size.width, size.height)
        lineTo(size.width, size.height - thicknessPx)
        lineTo(0F, size.height - thicknessPx)
    }
}