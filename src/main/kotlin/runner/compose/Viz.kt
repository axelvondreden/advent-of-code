package runner.compose

import androidx.compose.ui.graphics.Color

class Viz(val progress: Double? = null, val width: Int = 30, val height: Int = 10) {

    val map: Array<Array<Tile>> = Array(width) { Array(height) { Tile() } }

    fun set(x: Int, y: Int, vararg tiles: Tile) {
        for (i in tiles.indices) {
            map[x + i][y] = tiles[i]
        }
    }

}

class Tile(
    val char: Char? = null,
    val color: Color = Color.White,
    val backgroundColor: Color = Color(0xFF121212),
    val borderColor: Color = Color(30, 30, 30)
)

fun tileText(
    text: String,
    color: Color = Color.White,
    backgroundColor: Color = Color(0xFF121212),
    borderColor: Color = Color(30, 30, 30)
): Array<Tile> = text.map { Tile(it, color, backgroundColor, borderColor) }.toTypedArray()
