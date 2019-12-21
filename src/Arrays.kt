fun Array<BooleanArray>.on(p1: Point, p2: Point) =
    (p1.x..p2.x).forEach { x -> (p1.y..p2.y).forEach { y -> get(x)[y] = true } }

fun Array<BooleanArray>.off(p1: Point, p2: Point) =
    (p1.x..p2.x).forEach { x -> (p1.y..p2.y).forEach { y -> get(x)[y] = false } }

fun Array<BooleanArray>.toggle(p1: Point, p2: Point) =
    (p1.x..p2.x).forEach { x -> (p1.y..p2.y).forEach { y -> get(x)[y] = !get(x)[y] } }

fun Array<IntArray>.inc(p1: Point, p2: Point, amount: Int) =
    (p1.x..p2.x).forEach { x -> (p1.y..p2.y).forEach { y -> get(x)[y] += amount } }

fun Array<IntArray>.dec(p1: Point, p2: Point, amount: Int) =
    (p1.x..p2.x).forEach { x -> (p1.y..p2.y).forEach { y -> get(x)[y] = kotlin.math.max(get(x)[y] - amount, 0) } }

fun Array<BooleanArray>.copy() = map { it.clone() }.toTypedArray()