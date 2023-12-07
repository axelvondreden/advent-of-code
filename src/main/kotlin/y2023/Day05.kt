package y2023

import Day

class Day05 : Day<List<String>>(2023, 5) {

    private lateinit var seeds: List<Long>

    private lateinit var maps: List<MappingGroup>

    override fun List<String>.parse() = filter { it.isNotBlank() }.also {  lines ->
        seeds = lines[0].removePrefix("seeds: ").split(" ").map { it.toLong() }

        val list = mutableListOf<MappingGroup>()
        var group: MappingGroup? = null
        for (i in 1 until lines.size) {
            if (lines[i].contains(":")) {
                if (group != null) {
                    list += group
                }
                group = MappingGroup(emptyList())
            } else {
                val split = lines[i].split(" ")
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

    data class MappingGroup(val mappings: List<Mapping>) {

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

    data class Mapping(val destRangeStart: Long, val sourceRangeStart: Long, val length: Long)
}