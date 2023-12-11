package utils

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

fun Array<CharArray>.find(c: Char): Point? {
    for (y in get(0).indices) {
        for (x in indices) {
            if (get(x)[y] == c) return Point(x, y)
        }
    }
    return null
}

fun Array<CharArray>.findPoints(vararg c: Char): List<Point> {
    val list = mutableListOf<Point>()
    for (y in get(0).indices) {
        for (x in indices) {
            if (get(x)[y] in c) list += Point(x, y)
        }
    }
    return list
}

fun Array<CharArray>.print(xRange: IntRange = this.indices, yRange: IntRange = this[0].indices) {
    println()
    for (y in yRange) {
        for (x in xRange) {
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

fun Array<BooleanArray>.toMapString(): String {
    var s = ""
    for (y in get(0).indices) {
        for (x in indices) {
            s += if (get(x)[y]) '#' else '.'
        }
        s += "\n"
    }
    return s
}

fun Array<CharArray>.deepEquals(other: Array<CharArray>): Boolean {
    indices.forEach { y -> get(0).indices.forEach { x -> if (get(y)[x] != other[y][x]) return false } }
    return true
}

fun Array<CharArray>.pad(char: Char, all: Int) = pad(char, all, all, all, all)

fun Array<CharArray>.pad(char: Char, left: Int = 0, right: Int = 0, top: Int = 0, bottom: Int = 0) =
    Array(size + left + right) { x ->
        CharArray(this[0].size + top + bottom) { y ->
            if (x < left) char
            else if (x > lastIndex + left) char
            else if (y < top) char
            else if (y > this[0].lastIndex + top) char
            else this[x - left][y - top]
        }
    }

operator fun Array<CharArray>.get(point: Point) = this[point.x.toInt()][point.y.toInt()]

operator fun Array<CharArray>.set(point: Point, value: Char) {
    this[point.x.toInt()][point.y.toInt()] = value
}