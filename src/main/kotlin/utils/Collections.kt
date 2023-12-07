package utils

fun Collection<Long>.product(): Long {
    var product = 1L
    forEach { product *= it }
    return product
}

fun Collection<Int>.product(): Int {
    var product = 1
    forEach { product *= it }
    return product
}

fun <T> List<T>.permute(): List<List<T>> {
    if (size == 1) return listOf(this)
    val perms = mutableListOf<List<T>>()
    val sub = get(0)
    for (perm in drop(1).permute())
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, sub)
            perms.add(newPerm)
        }
    return perms
}

fun <T> Set<T>.permute(): List<List<T>> {
    if (size == 1) return listOf(toList())
    val perms = mutableListOf<List<T>>()
    val sub = first()
    for (perm in drop(1).permute())
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, sub)
            perms.add(newPerm)
        }
    return perms
}

fun List<String>.toCharMatrix(): Array<CharArray> {
    val max = maxOf { it.length }
    val matrix = Array(max) { CharArray(size) }
    for (y in indices) {
        val s = get(y)
        for (x in s.indices) {
            matrix[x][y] = s[x]
        }
    }
    return matrix
}

fun String.toIntArray(delim: String = ",") = split(delim.toRegex()).map(String::toInt).toIntArray()

fun String.toLongArray(delim: String = ",") = split(delim.toRegex()).map(String::toLong).toLongArray()