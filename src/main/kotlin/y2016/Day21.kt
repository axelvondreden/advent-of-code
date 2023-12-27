package y2016

import Day


class Day21 : Day<List<Day21.Operation>>(2016, 21) {

    override suspend fun List<String>.parse() = map {
        val split = it.split(" ")
        when {
            split[0] == "swap" && split[1] == "position" -> SwapPos(split[2].toInt(), split[5].toInt())
            split[0] == "swap" && split[1] == "letter" -> SwapLetter(split[2][0], split[5][0])
            split[0] == "rotate" && split[1] == "left" -> RotateLeft(split[2].toInt())
            split[0] == "rotate" && split[1] == "right" -> RotateRight(split[2].toInt())
            split[0] == "rotate" && split[1] == "based" -> RotateBasedOnLetter(split[6][0])
            split[0] == "reverse" -> ReversePositionRange(split[2].toInt(), split[4].toInt())
            split[0] == "move" -> MovePosition(split[2].toInt(), split[5].toInt())
            else -> throw RuntimeException("oh no!")
        }
    }

    override suspend fun solve1(input: List<Operation>) = "abcdefgh".scramble(input)

    override suspend fun solve2(input: List<Operation>) = "fbgdceah".unscramble(input)

    private fun String.scramble(ops: List<Operation>) = ops.fold(this) { op, input -> input.scramble(op) }

    private fun String.unscramble(ops: List<Operation>) = ops.reversed().fold(this) { op, input -> input.unscramble(op) }

    abstract class Operation {
        abstract fun scramble(str: String): String
        abstract fun unscramble(str: String): String
        abstract val vizString: String
    }

    private data class SwapPos(val pos1: Int, val pos2: Int) : Operation() {
        override fun scramble(str: String) = str.toCharArray().apply {
            val c = get(pos1)
            set(pos1, get(pos2))
            set(pos2, c)
        }.concatToString()

        override fun unscramble(str: String) = SwapPos(pos2, pos1).scramble(str)

        override val vizString = "swap $pos1 $pos2"
    }

    private data class SwapLetter(val letter1: Char, val letter2: Char) : Operation() {
        override fun scramble(str: String): String {
            val new = StringBuilder(str.length)
            for (c in str) {
                when (c) {
                    letter1 -> new.append(letter2)
                    letter2 -> new.append(letter1)
                    else -> new.append(c)
                }
            }
            return new.toString()
        }

        override fun unscramble(str: String) = SwapLetter(letter2, letter1).scramble(str)

        override val vizString = "swap $letter1 $letter2"
    }

    private data class RotateLeft(val steps: Int) : Operation() {
        override fun scramble(str: String) = str.substring(steps % str.length) + str.substring(0, steps % str.length)
        override fun unscramble(str: String) = RotateRight(steps).scramble(str)
        override val vizString = "rotate left $steps"
    }

    private data class RotateRight(val steps: Int) : Operation() {
        override fun scramble(str: String) =
            str.substring(str.length - (steps % str.length)) + str.substring(0, str.length - (steps % str.length))

        override fun unscramble(str: String) = RotateLeft(steps).scramble(str)
        override val vizString = "rotate right $steps"
    }

    private data class RotateBasedOnLetter(val letter: Char) : Operation() {
        override fun scramble(str: String): String {
            val index = str.indexOf(letter)
            val rotation = index + 1 + (if (index >= 4) 1 else 0)
            return RotateRight(rotation).scramble(str)
        }

        override fun unscramble(str: String): String {
            var testStr = str
            for (i in str.indices) {
                if (scramble(testStr) == str) {
                    return testStr
                }
                testStr = RotateRight(1).scramble(testStr)
            }
            throw RuntimeException("oh no!")
        }
        override val vizString = "rotate $letter"
    }

    private data class ReversePositionRange(val start: Int, val end: Int) : Operation() {
        override fun scramble(str: String) =
            str.substring(0, start) + str.substring(start, end + 1).reversed() + str.substring(end + 1)

        override fun unscramble(str: String) = scramble(str)

        override val vizString = "reverse $start $end"
    }

    private data class MovePosition(val source: Int, val target: Int) : Operation() {
        override fun scramble(str: String): String {
            val letter = str[source]
            val temp = str.removeRange(source, source + 1)
            return temp.substring(0, target) + letter + temp.substring(target)
        }

        override fun unscramble(str: String) = MovePosition(target, source).scramble(str)

        override val vizString = "move $source $target"
    }
}