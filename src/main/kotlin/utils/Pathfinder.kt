package utils

import java.util.*
import java.util.ArrayDeque
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

typealias Neighbors = (PFNode) -> Set<PFNode>

private fun neighbors(current: PFNode) = setOf(
    Point(current.x - 1, current.y),
    Point(current.x + 1, current.y),
    Point(current.x, current.y - 1),
    Point(current.x, current.y + 1)
)

class Pathfinder(
    val map: BooleanArray,
    private val width: Int,
    private val height: Int,
    private val neighborFunction: Neighbors = ::neighbors
) {
    private val fringe = PriorityQueue<PFNode> { o1, o2 -> sign(f(o1) - f(o2)).toInt() }
    private val closed = hashSetOf<PFNode>()

    private val gCosts = Array(map.size) { 0.0 }
    private val fCosts = Array(map.size) { 0.0 }

    fun searchAStar(start: PFNode, end: PFNode): List<PFNode> {
        fringe.clear()
        closed.clear()
        gCosts.fill(0.0)
        fCosts.fill(0.0)
        if (start == end) return listOf(start)

        g(start, 0.0)
        f(start, manhattan(start, end))
        fringe.add(start)

        while (!fringe.isEmpty()) {
            val current = fringe.poll() ?: break
            closed.add(current)
            if (current == end) return reconstructFrom(current)
            neighborFunction.invoke(current).forEach { neighbor ->
                neighbor.parent = current
                if (closed.contains(neighbor) || !bound(neighbor) || blocked(neighbor)) return@forEach
                val g0 = g(current) + manhattan(current, neighbor)
                if (!fringe.contains(neighbor)) fringe.add(neighbor)
                else if (g0 >= g(neighbor)) return@forEach
                g(neighbor, g0)
                f(neighbor, g0 + manhattan(neighbor, end))
            }
        }
        return emptyList()
    }

    fun searchBFS(start: PFNode, end: PFNode): List<PFNode> {
        val previous = hashMapOf<PFNode, PFNode>()

        val queue = ArrayDeque<PFNode>()
        val visited = hashSetOf<PFNode>()

        queue.offer(start)
        visited.add(start)
        while (!queue.isEmpty()) {
            val cell = queue.poll()
            if (cell == end) break

            neighborFunction.invoke(cell).forEach { newCell ->
                if (!bound(newCell) || blocked(newCell) || newCell in visited) return@forEach
                previous[newCell] = cell
                queue.offer(newCell)
                visited.add(newCell)
            }
        }
        return generateSequence(previous[end]) { cell -> previous[cell] }
            .takeWhile { cell -> cell != start }
            .toList()
            .ifEmpty { return emptyList() }.reversed()
    }

    private fun index(x: Long, y: Long) = (y * width + x).toInt()

    private fun f(node: PFNode) = fCosts[index(node.x, node.y)]

    private fun f(node: PFNode, value: Double) {
        fCosts[index(node.x, node.y)] = value
    }

    private fun g(node: PFNode) = gCosts[index(node.x, node.y)]

    private fun g(node: PFNode, value: Double) {
        gCosts[index(node.x, node.y)] = value
    }

    private fun bound(node: PFNode) = node.x >= 0 && node.y >= 0 && node.x < width && node.y < height

    private fun blocked(node: PFNode) = map[index(node.x, node.y)]

    private fun reconstructFrom(current: PFNode): List<PFNode> {
        val list = arrayListOf(current)
        var p = current.parent
        while (p != null) {
            list.add(p)
            p = p.parent
        }
        return list
    }

    private fun manhattan(node0: PFNode, node1: PFNode) =
        max(node0.x, node1.x) - min(node0.x, node1.x) + (max(node0.y, node1.y) - min(node0.y, node1.y)).toDouble()

    fun printMap(path: List<PFNode>, start: PFNode? = null, end: PFNode? = null) {
        println()
        (0 until height).forEach { y ->
            (0 until width).forEach { x ->
                if (x == start?.x?.toInt() && y == start.y.toInt()) print("\u001B[32mS\u001B[0m")
                if (x == end?.x?.toInt() && y == end.y.toInt()) print("\u001B[32mX\u001B[0m")
                if (path.contains(Point(x, y))) print("\u001B[32m*\u001B[0m")
                else print(if (map[y * width + x]) '#' else '.')
            }
            println()
        }
    }
}

abstract class PFNode(val x: Long, val y: Long) {
    var parent: PFNode? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PFNode
        if (x != other.x) return false
        if (y != other.y) return false
        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result * 31
    }
}