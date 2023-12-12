package y2023

import Day

class Day12 : Day<List<Day12.Row>>(2023, 12) {

    override fun List<String>.parse() = map { line ->
        val s = line.split(" ")
        Row(s[0].toCharArray(), s[1].split(",").map { it.toInt() })
    }

    override fun solve1(input: List<Row>) = input.sumOf { row ->
        val replacements =
            getPermutations(row.springs.count { it == '?' }, row.groups.sum() - row.springs.count { it == '#' })
        replacements.count {
            val replaced = row.springs.replaceUnknown(it)
            replaced.matches(row.groups)
        }
    }

    override fun solve2(input: List<Row>): Int {
        val unfolded = input.map { row ->
            Row(
                springs = row.springs.asSequence()
                    .plus('?').plus(row.springs)
                    .plus('?').plus(row.springs)
                    .plus('?').plus(row.springs)
                    .plus('?').plus(row.springs).toList(),
                groups = row.groups.plus(row.groups).plus(row.groups).plus(row.groups).plus(row.groups)
            )
        }
        return input.sumOf { row ->
            val replacements =
                getPermutations(row.springs.count { it == '?' }, row.groups.sum() - row.springs.count { it == '#' })
            replacements.count {
                val replaced = row.springs.replaceUnknown(it)
                replaced.matches(row.groups)
            }
        }
    }

    private fun CharArray.countMatches(next: Char, groups: List<Int>): Int {
        val qIndex = indexOf('?')
        if (qIndex >= 0) {
            val new = this.copyOf()
            new[qIndex] = next
            return new.countMatches('#', groups) + new.countMatches('.', groups)
        } else {
            return if (matches(groups)) 1 else 0
        }
    }

    private fun getPermutations(length: Int, brokeCount: Int) =
        List(length) { if (it < brokeCount) '#' else '.' }.permute()

    data class Row(val springs: CharArray, val groups: List<Int>)

    private fun CharArray.matches(groups: List<Int>): Boolean {
        val sizes = mutableListOf<Int>()
        var currentSize = 0
        for (c in this) {
            if (c == '#') {
                currentSize++
            } else if (currentSize > 0) {
                sizes += currentSize
                currentSize = 0
            }
        }
        if (currentSize > 0) {
            sizes += currentSize
        }
        return sizes.size == groups.size && sizes.withIndex().all { it.value == groups[it.index] }
    }

    private fun List<Char>.replaceUnknown(replacement: List<Char>): List<Char> {
        var index = 0
        val result = toMutableList()
        for (i in indices) {
            if (this[i] == '?') {
                result[i] = replacement[index]
                index++
            }
        }
        return result
    }

    private fun List<Char>.permute(): Set<List<Char>> {
        if (size == 1) return setOf(this)
        val perms = mutableSetOf<List<Char>>()
        val sub = get(0)
        for (perm in drop(1).permute())
            for (i in 0..perm.size) {
                val newPerm = perm.toMutableList()
                newPerm.add(i, sub)
                perms.add(newPerm)
            }
        return perms
    }
}
