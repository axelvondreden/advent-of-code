package runner.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class VizGrid(progress: Double? = null, val width: Int = 30, val height: Int = 10) : Viz(progress) {

    val map: Array<Array<Tile>> = Array(width) { Array(height) { Tile() } }

    fun backgroundColor(x: Int, y: Int, color: Color) {
        map[x][y].backgroundColor = color
    }

    fun backgroundColor(from: Pair<Int, Int>, to: Pair<Int, Int>, color: Color) {
        for (x in from.first..to.first) {
            for (y in from.second..to.second) {
                backgroundColor(x, y, color)
            }
        }
    }

    fun border(x: Int, y: Int, color: Color, shape: Shape = RectangleShape) {
        map[x][y].borderColor = color
        map[x][y].borderShape = { shape }
    }

    fun border(from: Pair<Int, Int>, to: Pair<Int, Int>, color: Color, shape: Shape = RectangleShape) {
        for (x in from.first..to.first) {
            for (y in from.second..to.second) {
                border(x, y, color, shape)
            }
        }
    }

    fun grid(x: Int, y: Int, grid: Array<Array<Tile>>) {
        for (localX in grid.indices) {
            for (localY in grid[localX].indices) {
                map[x + localX][y + localY] = grid[localX][localY]
            }
        }
    }

    fun text(
        x: Int,
        y: Int,
        text: String,
        color: Color = Color.White,
        backgroundColor: Color = Color(0xFF121212),
        borderColor: Color = Color(30, 30, 30)
    ) {
        if (text.length <= 1) {
            with(map[x][y]) {
                char = text.firstOrNull()
                this.color = color
                this.backgroundColor = backgroundColor
                this.borderColor = borderColor
            }
        } else {
            with(map[x][y]) {
                char = text.first()
                this.color = color
                this.backgroundColor = backgroundColor
                this.borderColor = borderColor
                borderShape = { BorderLeftTopBottomShape() }
            }
            for (i in 1 until text.lastIndex) {
                with(map[x + i][y]) {
                    char = text[i]
                    this.color = color
                    this.backgroundColor = backgroundColor
                    this.borderColor = borderColor
                    borderShape = { BorderTopBottomShape() }
                }
            }
            with(map[x + text.lastIndex][y]) {
                char = text.last()
                this.color = color
                this.backgroundColor = backgroundColor
                this.borderColor = borderColor
                borderShape = { BorderRightTopBottomShape() }
            }
        }
    }

    @Composable
    override fun drawViz() {
        Row(Modifier.fillMaxSize().padding(6.dp)) {
            for (x in 0 until width) {
                Column(modifier = Modifier.fillMaxHeight().weight(1F)) {
                    for (y in 0 until height) {
                        val tile = map[x][y]
                        Tile(tile)
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.Tile(tile: Tile) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1F)
            .border(1.dp, tile.borderColor, shape = tile.borderShape())
            .background(tile.backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            tile.char?.toString() ?: "",
            color = tile.color,
            fontSize = maxWidth.value.sp / 3,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

data class Tile(
    var char: Char? = null,
    var color: Color = Color.White,
    var backgroundColor: Color = Color(0xFF121212),
    var borderColor: Color = Color(30, 30, 30),
    var borderShape: @Composable () -> Shape = { RectangleShape }
)