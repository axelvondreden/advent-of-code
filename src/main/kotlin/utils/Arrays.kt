package utils

import pathfinding.Pathfinder

fun Array<BooleanArray>.on(p1: Point, p2: Point) =
    (p1.x..p2.x).forEach { x -> (p1.y..p2.y).forEach { y -> get(x.toInt())[y.toInt()] = true } }

fun Array<BooleanArray>.off(p1: Point, p2: Point) =
    (p1.x..p2.x).forEach { x -> (p1.y..p2.y).forEach { y -> get(x.toInt())[y.toInt()] = false } }

fun Array<BooleanArray>.toggle(p1: Point, p2: Point) =
    (p1.x..p2.x).forEach { x -> (p1.y..p2.y).forEach { y -> get(x.toInt())[y.toInt()] = !get(x.toInt())[y.toInt()] } }

fun Array<IntArray>.inc(p1: Point, p2: Point, amount: Int) =
    (p1.x..p2.x).forEach { x -> (p1.y..p2.y).forEach { y -> get(x.toInt())[y.toInt()] += amount } }

fun Array<IntArray>.dec(p1: Point, p2: Point, amount: Int) =
    (p1.x..p2.x).forEach { x -> (p1.y..p2.y).forEach { y -> get(x.toInt())[y.toInt()] = kotlin.math.max(get(x.toInt())[y.toInt()] - amount, 0) } }

fun Array<BooleanArray>.copy() = map { it.clone() }.toTypedArray()

fun Array<CharArray>.copy() = map { it.clone() }.toTypedArray()

fun Array<CharArray>.toPathfindingMap(walls: Set<Char> = setOf('#')): BooleanArray {
    val arr = BooleanArray(size * get(0).size)
    for (y in get(0).indices) {
        for (x in indices) {
            arr[y * size + x] = get(x)[y] in walls
        }
    }
    return arr
}

fun Array<CharArray>.find(c: Char): Pathfinder.Node? {
    for (y in get(0).indices) {
        for (x in indices) {
            if (get(x)[y] == c) return Pathfinder.Node(x, y)
        }
    }
    return null
}

fun Array<CharArray>.print() {
    println()
    for (y in get(0).indices) {
        for (x in indices) {
            print(get(x)[y])
        }
        println()
    }
}

fun Array<IntArray>.print() {
    println()
    for (y in get(0).indices) {
        for (x in indices) {
            print(get(x)[y])
        }
        println()
    }
}

fun Array<BooleanArray>.print() {
    println()
    for (y in get(0).indices) {
        for (x in indices) {
            print(if (get(x)[y]) '#' else '.')
        }
        println()
    }
}

fun Array<CharArray>.deepEquals(other: Array<CharArray>): Boolean {
    indices.forEach { y -> get(0).indices.forEach { x -> if (get(y)[x] != other[y][x]) return false } }
    return true
}