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
        Turn.AROUND -> when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}

enum class Turn {
    LEFT, STRAIGHT, RIGHT, AROUND
}