package runner

import androidx.compose.ui.graphics.Color

class Viz(val progress: Double? = null, val size: Int = 32) {

    private val map: Array<Tile> = Array(size * size) { Tile() }

    private class Tile(
        val char: Char? = null,
        val color: Color = Color.White,
        val bgColor: Color = Color(0xFF121212)
    )
}
