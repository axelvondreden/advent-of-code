package utils

enum class Dir {
    UP, DOWN, LEFT, RIGHT;

    fun turn(turn: Turn) = when (turn) {
        Turn.LEFT -> when (this) {
            UP -> LEFT
            DOWN -> RIGHT
            LEFT -> DOWN
            RIGHT -> UP
        }
        Turn.STRAIGHT -> this
        Turn.RIGHT -> when (this) {
            UP -> RIGHT
            DOWN -> LEFT
            LEFT -> UP
            RIGHT -> DOWN
        }
    }
}

enum class Turn {
    LEFT, STRAIGHT, RIGHT
}