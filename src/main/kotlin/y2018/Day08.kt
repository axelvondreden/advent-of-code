package y2018

import Day

class Day08 : Day(2018, 8) {

    override val input = readString().split(" ").map { it.toInt() }.iterator().toNode()

    override fun solve1() = input.sumMetadata

    override fun solve2() = input.value

    private fun Iterator<Int>.toNode(): Node {
        val childCount = next()
        val metaCount = next()
        val children = (0 until childCount).map { toNode() }
        val metadata = (0 until metaCount).map { Metadata(next()) }
        return Node(children, metadata)
    }

    data class Node(val children: List<Node>, val metadata: List<Metadata>) {
        val sumMetadata get(): Int = metadata.sumBy { it.value } + children.sumBy { it.sumMetadata }
        val value: Int
            get(): Int {
                return if (children.isEmpty()) sumMetadata
                else metadata.filter { it.value - 1 in children.indices }.sumBy { children[it.value - 1].value }
            }
    }

    data class Metadata(val value: Int)
}