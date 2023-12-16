package runner.compose

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape

class Viz(val progress: Double? = null, val width: Int = 30, val height: Int = 10) {

    val map: Array<Array<Tile>> = Array(width) { Array(height) { Tile() } }

    fun text(
        x: Int,
        y: Int,
        text: String,
        color: Color = Color.White,
        backgroundColor: Color = Color(0xFF121212),
        borderColor: Color = Color(30, 30, 30)
    ) {
        for (i in text.indices) {
            with(map[x + i][y]) {
                char = text[i]
                this.color = color
                this.backgroundColor = backgroundColor
                this.borderColor = borderColor
            }
        }
    }

}

data class Tile(
    var char: Char? = null,
    var color: Color = Color.White,
    var backgroundColor: Color = Color(0xFF121212),
    var borderColor: Color = Color(30, 30, 30),
    var borderShape: Shape = RectangleShape
)