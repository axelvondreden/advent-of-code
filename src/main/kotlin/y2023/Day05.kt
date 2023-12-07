package y2023

import Day

class Day05 : Day<Any?>(2023, 5) {

    override val input = readStrings().filter { it.isNotBlank() }

    private val seeds = input[0].removePrefix("seeds: ").split(" ").map { it.toLong() }

    private val maps: List<MappingGroup>

    init {
        val list = mutableListOf<MappingGroup>()
        var group: MappingGroup? = null
        for (i in 1 until input.size) {
            if (input[i].contains(":")) {
                if (group != null) {
                    list += group
                }
                group = MappingGroup(emptyList())
            } else {
                val split = input[i].split(" ")
                group = group!!.copy(
                    mappings = group.mappings.plus(
                        Mapping(
                            split[0].toLong(),
                            split[1].toLong(),
                            split[2].toLong()
                        )
                    )
                )
            }
        }
        list += group!!
        maps = list
    }

    override fun solve1(input: List<String>): Long {
        val values = seeds.toTypedArray()
        maps.forEach { map ->
            values.indices.forEach {
                values[it] = map.map(values[it])
            }
        }
        return values.min()
    }

    override fun solve2(input: List<String>): Long {
        val values = seeds.chunked(2).map { (it[0] until it[0] + it[1]).toList() }.flatten().toTypedArray()
        maps.forEach { map ->
            values.indices.forEach {
                values[it] = map.map(values[it])
            }
        }
        return values.min()
    }

    private data class MappingGroup(val mappings: List<Mapping>) {

        fun map(source: Long): Long {
            mappings.forEach { m ->
                if (source >= m.sourceRangeStart && source < m.sourceRangeStart + m.length) {
                    val index = source - m.sourceRangeStart
                    if (index >= 0) {
                        return m.destRangeStart + index
                    }
                }
            }
            return source
        }
    }

    private data class Mapping(val destRangeStart: Long, val sourceRangeStart: Long, val length: Long)
}