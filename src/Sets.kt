fun Set<Long>.product(): Long {
    var product = 1L
    forEach { product *= it }
    return product
}