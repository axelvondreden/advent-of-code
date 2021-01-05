package y2015

import Day
import kotlin.math.max
import kotlin.math.min

class Day21 : Day(2015, 21) {

    override val input = with(readStrings().map { it.split(": ")[1].toInt() }) { Triple(get(0), get(1), get(2)) }

    override fun solve1(): Int {
        var minGold = Integer.MAX_VALUE
        weapons.forEach { weapon ->
            if (simulate(You(weapon, null, emptySet()), Boss(input.first, input.second, input.third))) {
                minGold = min(minGold, weapon.cost)
            }
            armors.forEach { armor ->
                if (simulate(You(weapon, armor, emptySet()), Boss(input.first, input.second, input.third))) {
                    minGold = min(minGold, weapon.cost + armor.cost)
                }
                rings.forEach { ring ->
                    if (simulate(You(weapon, armor, setOf(ring)), Boss(input.first, input.second, input.third))) {
                        minGold = min(minGold, weapon.cost + armor.cost + ring.cost)
                    }
                    rings.filter { it != ring }.forEach { ring2 ->
                        if (simulate(
                                You(weapon, armor, setOf(ring, ring2)),
                                Boss(input.first, input.second, input.third)
                            )
                        ) {
                            minGold = min(minGold, weapon.cost + armor.cost + ring.cost + ring2.cost)
                        }
                    }
                }
            }
        }
        return minGold
    }

    override fun solve2(): Int {
        var maxGold = 0
        weapons.filterNot { simulate(You(it, null, emptySet()), Boss(input.first, input.second, input.third)) }.forEach { weapon ->
            maxGold = max(maxGold, weapon.cost)
            rings.filterNot {
                simulate(You(weapon, null, setOf(it)), Boss(input.first, input.second, input.third))
            }.forEach { ring ->
                maxGold = max(maxGold, weapon.cost + ring.cost)
                rings.filter { it != ring }.filterNot {
                    simulate(You(weapon, null, setOf(ring, it)), Boss(input.first, input.second, input.third))
                }.forEach { maxGold = max(maxGold, weapon.cost + ring.cost + it.cost) }
            }
            armors.filterNot { simulate(You(weapon, it, emptySet()), Boss(input.first, input.second, input.third)) }.forEach { armor ->
                maxGold = max(maxGold, weapon.cost + armor.cost)
                rings.filterNot { simulate(You(weapon, armor, setOf(it)), Boss(input.first, input.second, input.third)) }.forEach { ring ->
                    maxGold = max(maxGold, weapon.cost + armor.cost + ring.cost)
                    rings.filter { it != ring }.filterNot {
                        simulate(You(weapon, armor, setOf(ring, it)), Boss(input.first, input.second, input.third))
                    }.forEach { maxGold = max(maxGold, weapon.cost + armor.cost + ring.cost + it.cost) }
                }
            }
        }
        return maxGold
    }

    private fun simulate(player: You, boss: Boss): Boolean {
        var playerTurn = true
        while (player.hp > 0 && boss.hp > 0) {
            if (playerTurn) boss.defend(player.attack()) else player.defend(boss.attack())
            playerTurn = !playerTurn
        }
        return player.hp > 0
    }

    companion object {
        private val weapons = setOf(
            Item(8, 4, 0),
            Item(10, 5, 0),
            Item(25, 6, 0),
            Item(40, 7, 0),
            Item(74, 8, 0)
        )
        private val armors = setOf(
            Item(13, 0, 1),
            Item(31, 0, 2),
            Item(53, 0, 3),
            Item(75, 0, 4),
            Item(102, 0, 5)
        )
        private val rings = setOf(
            Item(25, 1, 0),
            Item(50, 2, 0),
            Item(100, 3, 0),
            Item(20, 0, 1),
            Item(40, 0, 2),
            Item(80, 0, 3)
        )
    }

    private abstract class Player(var hp: Int) {
        abstract fun defend(dmg: Int)
        abstract fun attack(): Int
    }

    private class Boss(hp: Int, private val dmg: Int, private val arm: Int) : Player(hp) {

        override fun defend(dmg: Int) {
            hp -= max(dmg - arm, 1)
        }

        override fun attack() = dmg
    }

    private class You(private val weapon: Item, private val armor: Item?, private val rings: Set<Item>) : Player(100) {

        override fun defend(dmg: Int) {
            hp -= max(dmg - weapon.arm - (armor?.arm ?: 0) - rings.sumBy { it.arm }, 1)
        }

        override fun attack() = weapon.dmg + (armor?.dmg ?: 0) + rings.sumBy { it.dmg }
    }

    private data class Item(val cost: Int, val dmg: Int, val arm: Int)
}