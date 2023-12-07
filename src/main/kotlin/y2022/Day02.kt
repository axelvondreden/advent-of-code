package y2022

import Day

class Day02 : Day<List<Pair<Char, Char>>>(2022, 2) {

    override fun List<String>.parse() = map { it[0] to it[2] }

    override fun solve1(input: List<Pair<Char, Char>>) = input.sumOf {
        val move = when (it.second) {
            'X' -> 1
            'Y' -> 2
            else -> 3
        }
        move + when (it.first.play(it.second)) {
            Res.WIN -> 6
            Res.LOSE -> 0
            Res.DRAW -> 3
        }
    }

    override fun solve2(input: List<Pair<Char, Char>>) = input.sumOf {
        when (it.second) {
            'X' -> when (it.first) {
                'A' -> 3
                'B' -> 1
                else -> 2
            }
            'Y' -> 3 + when (it.first) {
                'A' -> 1
                'B' -> 2
                else -> 3
            }
            else -> 6 + when (it.first) {
                'A' -> 2
                'B' -> 3
                else -> 1
            }
        }
    }

    private fun Char.play(my: Char) = when (this) {
        'A' -> {
            when (my) {
                'X' -> Res.DRAW
                'Y' -> Res.WIN
                else -> Res.LOSE
            }
        }
        'B' -> {
            when (my) {
                'X' -> Res.LOSE
                'Y' -> Res.DRAW
                else -> Res.WIN
            }
        }
        else -> {
            when (my) {
                'X' -> Res.WIN
                'Y' -> Res.LOSE
                else -> Res.DRAW
            }
        }
    }

    private enum class Res {
        WIN, LOSE, DRAW
    }
}