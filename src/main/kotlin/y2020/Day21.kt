package y2020

import Day


class Day21 : Day(2020, 21) {

    override val input = readStrings().toIngredients()

    override fun solve1(): Int {
        val safe = input.safeIngredients()
        return input.keys.sumOf { food -> food.count { it in safe } }
    }

    override fun solve2(): String {
        val ingredientsByAllergy = input.ingredientsByAllergy()
        val found = mutableMapOf<String, String>()

        while (ingredientsByAllergy.isNotEmpty()) {
            val singles = ingredientsByAllergy
                .filter { it.value.size == 1 }
                .map { it.key to it.value.first() }
                .toMap()
            found.putAll(singles)
            singles.keys.forEach { ingredientsByAllergy.remove(it) }
            ingredientsByAllergy.values.forEach { it.removeAll(singles.values) }
        }
        return found.entries.sortedBy { it.key }.joinToString(",") { it.value }
    }

    private fun Map<Set<String>, Set<String>>.ingredientsByAllergy(): MutableMap<String, MutableSet<String>> {
        val safeIngredients = safeIngredients()
        return values.flatten().toSet().map { allergen ->
            allergen to entries
                .filter { allergen in it.value }
                .map { it.key - safeIngredients }
                .reduce { a, b -> a intersect b }
                .toMutableSet()
        }.toMap().toMutableMap()
    }

    private fun Map<Set<String>, Set<String>>.safeIngredients() = keys.flatten().toSet().subtract(
        values.flatten().toSet().flatMap { allergen ->
            filter { allergen in it.value }
                .map { it.key }
                .reduce { carry, ingredients -> ingredients intersect carry }
        }.toSet()
    )

    private fun List<String>.toIngredients() = map { line ->
        val ingredients = line.substringBefore(" (").split(" ").toSet()
        val allergens = line.substringAfter("(contains ").substringBefore(")").split(", ").toSet()
        ingredients to allergens
    }.toMap()
}