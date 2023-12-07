package y2021

import Day

class Day08 : Day<List<String>>(2021, 8) {

    override val input = readStrings().map { line ->
        InputLine(line.split(" ").filterNot { it == "|" }.map { it.toSet() })
    }

    override fun solve1(input: List<String>) = input.sumOf { line -> line.inputs.takeLast(4).count { it.size <= 4 || it.size == 7 } }

    override fun solve2(input: List<String>) = input.sumOf { it.calculateValue() }

    data class InputLine(val inputs: List<Set<Char>>) {
        private val digitValues = discoverMappings()

        fun calculateValue(): Int =
            (digitValues.getValue(inputs[10]) * 1000) +
                    (digitValues.getValue(inputs[11]) * 100) +
                    (digitValues.getValue(inputs[12]) * 10) +
                    digitValues.getValue(inputs[13])


        private fun discoverMappings(): Map<Set<Char>, Int> {
            val digitToString = Array<Set<Char>>(10) { emptySet() }

            // Unique based on size
            digitToString[1] = inputs.first { it.size == 2 }
            digitToString[4] = inputs.first { it.size == 4 }
            digitToString[7] = inputs.first { it.size == 3 }
            digitToString[8] = inputs.first { it.size == 7 }

            // 3 is length 5 and overlaps 1
            digitToString[3] = inputs
                .filter { it.size == 5 }
                .first { it overlaps digitToString[1] }

            // 9 is length 6 and overlaps 3
            digitToString[9] = inputs
                .filter { it.size == 6 }
                .first { it overlaps digitToString[3] }

            // 0 is length 6, overlaps 1 and 7, and is not 9
            digitToString[0] = inputs
                .filter { it.size == 6 }
                .filter { it overlaps digitToString[1] && it overlaps digitToString[7] }
                .first { it != digitToString[9] }

            // 6 is length 6 and is not 0 or 9
            digitToString[6] = inputs
                .filter { it.size == 6 }
                .first { it != digitToString[0] && it != digitToString[9] }

            // 5 is length 5 and is overlapped by 6
            digitToString[5] = inputs
                .filter { it.size == 5 }
                .first { digitToString[6] overlaps it }

            // 2 is length 5 and is not 3 or 5
            digitToString[2] = inputs
                .filter { it.size == 5 }
                .first { it != digitToString[3] && it != digitToString[5] }

            return digitToString.mapIndexed { index, chars -> chars to index }.toMap()
        }

        private infix fun Set<Char>.overlaps(that: Set<Char>) = this.containsAll(that)
    }
}