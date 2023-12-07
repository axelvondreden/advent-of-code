package y2020

import Day


class Day21 : Day<Map<Set<String>, Set<String>>>(2020, 21) {

    override fun List<String>.parse() = associate { line ->
        val ingredients = line.substringBefore(" (").split(" ").toSet()
        val allergens = line.substringAfter("(contains ").substringBefore(")").split(", ").toSet()
        ingredients to allergens
    }

    override fun solve1(input: Map<Set<String>, Set<String>>): Int {
        val safe = input.safeIngredients()
        return input.keys.sumOf { food -> food.count { it in safe } }
    }

    override fun solve2(input: Map<Set<String>, Set<String>>): String {
        val ingredientsByAllergy = input.ingredientsByAllergy()
        val found = mutableMapOf<String, String>()

        while (ingredientsByAllergy.isNotEmpty()) {
            val singles = ingredientsByAllergy
                .filter { it.value.size == 1 }
                .map { it.key to it.value.first() }
                .toMap()
            found.putAll(singles)
            singles.keys.forEach { ingredientsByAllergy.remove(it) }
            ingredientsByAllergy.values.forEach { it.removeAll(singles.values.toSet()) }
        }
        return found.entries.sortedBy { it.key }.joinToString(",") { it.value }
    }

    private fun Map<Set<String>, Set<String>>.ingredientsByAllergy(): MutableMap<String, MutableSet<String>> {
        val safeIngredients = safeIngredients()
        return values.flatten().toSet().associateWith { allergen ->
            entries.filter { allergen in it.value }
                .map { it.key - safeIngredients }
                .reduce { a, b -> a intersect b }
                .toMutableSet()
        }.toMutableMap()
    }

    private fun Map<Set<String>, Set<String>>.safeIngredients() = keys.flatten().toSet().subtract(
        values.flatten().toSet().flatMap { allergen ->
            filter { allergen in it.value }
                .map { it.key }
                .reduce { carry, ingredients -> ingredients intersect carry }
        }.toSet()
    )
}