package pathfinding

import pathfinding.Pathfinder.*
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

typealias Neighbors = (INode) -> Set<INode>

private fun neighbors(current: INode) = setOf(
        Node(current.x - 1, current.y),
        Node(current.x + 1, current.y),
        Node(current.x, current.y - 1),
        Node(current.x, current.y + 1)
)

class Pathfinder(val map: BooleanArray, private val width: Int, private val height: Int, private val neighborFunction: Neighbors = ::neighbors) {
    private val fringe = PriorityQueue<INode> { o1, o2 -> sign(f(o1) - f(o2)).toInt() }
    private val closed = hashSetOf<INode>()

    private val gCosts = Array(map.size) { 0.0 }
    private val fCosts = Array(map.size) { 0.0 }

    fun searchAStar(start: INode, end: INode): List<INode> {
        if (start == end) return listOf(start)

        g(start, 0.0)
        f(start, manhattan(start, end))
        fringe.add(start)

        while (!fringe.isEmpty()) {
            val current = fringe.poll() ?: break
            closed.add(current)
            if (current == end) return reconstructFrom(current)
            for (neighbor in neighborFunction.invoke(current)) {
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

    fun searchBFS(start: INode, end: INode): List<INode> {
        val previous = hashMapOf<INode, INode>()

        val queue = ArrayDeque<INode>()
        val visited = hashSetOf<INode>()

        queue.offer(start)
        visited.add(start)
        while (!queue.isEmpty()) {
            val cell = queue.poll()
            if (cell == end) break

            for (newCell in neighborFunction.invoke(cell)) {
                if (!bound(newCell) || blocked(newCell) || newCell in visited) continue
                previous[newCell] = cell
                queue.offer(newCell)
                visited.add(newCell)
            }
        }

        val pathToStart =
                generateSequence(previous[end]) { cell -> previous[cell] }
                        .takeWhile { cell -> cell != start }
                        .toList()
                        .ifEmpty { return emptyList() }
        return pathToStart.reversed()
    }

    private fun index(x: Int, y: Int) = y * width + x

    private fun f(node: INode) = fCosts[index(node.x, node.y)]

    private fun f(node: INode, value: Double) {
        fCosts[index(node.x, node.y)] = value
    }

    private fun g(node: INode) = gCosts[index(node.x, node.y)]

    private fun g(node: INode, value: Double) {
        gCosts[index(node.x, node.y)] = value
    }

    private fun bound(node: INode) = node.x >= 0 && node.y >= 0 && node.x < width && node.y < height

    private fun blocked(node: INode) = map[index(node.x, node.y)]

    private fun reconstructFrom(current: INode): List<INode> {
        val list = arrayListOf(current)
        var p = current.parent
        while (p != null) {
            list.add(p)
            p = p.parent
        }

        return list
    }

    private fun manhattan(node0: INode, node1: INode) = max(node0.x, node1.x) - min(node0.x, node1.x) + (max(node0.y, node1.y) - min(node0.y, node1.y)).toDouble()

    fun printMap(path: List<INode>) {
        println()
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (path.contains(Node(x, y))) print("\u001B[32m*\u001B[0m")
                else print(if (map[y * width + x]) '#' else ' ')
            }
            println()
        }
    }

    class Node(x: Int, y: Int) : INode(x, y)

    abstract class INode(val x: Int, val y: Int) {
        var parent: INode? = null

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as INode

            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }
    }
}