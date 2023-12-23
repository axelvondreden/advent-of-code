package runner.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import utils.FloatPoint

class VizCanvas(progress: Double? = null) : Viz(progress) {

    private val lines = mutableListOf<Line>()

    @Composable
    override fun drawViz() {
        Spacer(Modifier.fillMaxSize().safeDrawingPadding().drawWithContent {

            lines.forEach { line ->
                drawLine(
                    color = line.color,
                    start = Offset(size.width * line.start.x, size.height * line.start.y),
                    end = Offset(size.width * line.end.x, size.height * line.end.y),
                    strokeWidth = line.strokeWidth,
                    alpha = line.alpha
                )
            }
        })
    }

    fun line(color: Color, start: FloatPoint, end: FloatPoint, strokeWidth: Float = Stroke.HairlineWidth, alpha: Float = 1F) {
        lines.add(Line(color, start, end, strokeWidth, alpha))
    }

    private data class Line(
        val color: Color,
        val start: FloatPoint,
        val end: FloatPoint,
        val strokeWidth: Float,
        val alpha: Float,
    )
}