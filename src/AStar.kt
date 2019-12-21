import java.util.*
import kotlin.math.*

typealias Heuristic = (AStar.Node, AStar.Node) -> Double

class AStar(val map: BooleanArray, private val width: Int, private val height: Int) {
    private val fringe = PriorityQueue<Node> { o1, o2 -> sign(f(o1) - f(o2)).toInt() }
    private val closed = hashSetOf<Node>()

    private val gCosts = Array(map.size) { 0.0 }
    private val fCosts = Array(map.size) { 0.0 }

    fun search(start: Node, end: Node): List<Node> {
        if (start == end) return listOf(start)

        g(start, 0.0)
        f(start, manhattan(start, end))
        fringe.add(start)

        while (!fringe.isEmpty()) {
            val current = fringe.poll() ?: break
            closed.add(current)
            if (current == end) return reconstructFrom(current)
            val neighbors = arrayOf(
                Node(current.x - 1, current.y),
                Node(current.x + 1, current.y),
                Node(current.x, current.y - 1),
                Node(current.x, current.y + 1)
            )
            for (neighbor in neighbors) {
                neighbor.parent = current
                if (closed.contains(neighbor) || !bound(neighbor) || blocked(neighbor)) continue
                val g0 = g(current) + manhattan(current, neighbor)
                if (!fringe.contains(neighbor)) fringe.add(neighbor)
                else if (g0 >= g(neighbor)) continue
                g(neighbor, g0)
                f(neighbor, g0 + manhattan(neighbor, end))
            }
        }
        return emptyList()
    }

    private fun index(x: Int, y: Int) = y * width + x

    private fun f(node: Node) = fCosts[index(node.x, node.y)]

    private fun f(node: Node, value: Double) {
        fCosts[index(node.x, node.y)] = value
    }

    private fun g(node: Node) = gCosts[index(node.x, node.y)]

    private fun g(node: Node, value: Double) {
        gCosts[index(node.x, node.y)] = value
    }

    private fun bound(node: Node) = node.x >= 0 && node.y >= 0 && node.x < width && node.y < height

    private fun blocked(node: Node) = map[index(node.x, node.y)]

    private fun reconstructFrom(current: Node): List<Node> {
        val list = arrayListOf(current)
        var p = current.parent
        while (p != null) {
            list.add(p)
            p = p.parent
        }

        return list
    }

    private fun manhattan(node0: Node, node1: Node) = (max(node0.x, node1.x) - min(node0.x, node1.x)) + (max(node0.y, node1.y) - min(node0.y, node1.y)).toDouble()

    fun printMap(path: List<Node>) {
        println()
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (path.contains(Node(x, y))) print('*')
                else print(if (map[y * width + x]) '#' else ' ')
            }
            println()
        }
    }

    data class Node(val x: Int, val y: Int) {
        var parent: Node? = null
    }
}