package y2018

import Day

class Day08 : Day<Day08.Node>(2018, 8) {

    override suspend fun List<String>.parse() = first().split(" ").map { it.toInt() }.iterator().toNode()

    override suspend fun solve1(input: Node) = input.sumMetadata

    override suspend fun solve2(input: Node) = input.value

    private fun Iterator<Int>.toNode(): Node {
        val childCount = next()
        val metaCount = next()
        val children = (0 until childCount).map { toNode() }
        val metadata = (0 until metaCount).map { Metadata(next()) }
        return Node(children, metadata)
    }

    data class Node(val children: List<Node>, val metadata: List<Metadata>) {
        val sumMetadata get(): Int = metadata.sumOf { it.value } + children.sumOf { it.sumMetadata }
        val value: Int
            get(): Int {
                return if (children.isEmpty()) sumMetadata
                else metadata.filter { it.value - 1 in children.indices }.sumOf { children[it.value - 1].value }
            }
    }

    data class Metadata(val value: Int)
}