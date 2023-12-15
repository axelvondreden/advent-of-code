package runner

import androidx.compose.ui.graphics.Color

class Viz(val progress: Double? = null, val width: Int = 30, val height: Int = 10) {

    val map: Array<Array<Tile>> = Array(width) { Array(height) { Tile() } }

    class Tile(
        val char: Char? = null,
        val color: Color = Color.White,
        val backgroundColor: Color = Color(0xFF121212),
        val borderColor: Color = Color.DarkGray
    )
}
