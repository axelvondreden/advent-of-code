package y2023

import Day
import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultUndirectedGraph

class Day25 : Day<DefaultUndirectedGraph<String, DefaultEdge>>(2023, 25) {

    override suspend fun List<String>.parse(): DefaultUndirectedGraph<String, DefaultEdge> {
        val graph = DefaultUndirectedGraph<String, DefaultEdge>(DefaultEdge::class.java)
        forEach { line ->
            val nodes = line.split(":")
            val root = nodes.first()
            graph.addVertex(root)
            for (i in nodes.last().split(" ").filter { it.isNotBlank() }) {
                graph.addVertex(i)
                graph.addEdge(root, i)
            }
        }
        return graph
    }

    override suspend fun solve1(input: DefaultUndirectedGraph<String, DefaultEdge>): Int {
        val minCut = StoerWagnerMinimumCut(input).minCut()
        input.removeAllVertices(minCut)
        return input.vertexSet().size * minCut.size
    }

    override suspend fun solve2(input: DefaultUndirectedGraph<String, DefaultEdge>) = 0
}