package y2015

import Day
import kotlin.math.max
import kotlin.math.min

class Day21 : Day() {

    override val input = Triple(104, 8, 1)

    override fun solve1(): Int {
        var minGold = Integer.MAX_VALUE
        for (weapon in weapons) {
            if (simulate(
                    You(weapon, null, emptySet()),
                    Boss(input.first, input.second, input.third)
                )) {
                minGold = min(minGold, weapon.cost)
            }
            for (armor in armors) {
                if (simulate(
                        You(weapon, armor, emptySet()),
                        Boss(input.first, input.second, input.third)
                    )) {
                    minGold = min(minGold, weapon.cost + armor.cost)
                }
                for (ring in rings) {
                    if (simulate(
                            You(weapon, armor, setOf(ring)),
                            Boss(input.first, input.second, input.third)
                        )) {
                        minGold = min(minGold, weapon.cost + armor.cost + ring.cost)
                    }
                    for (ring2 in rings.filter { it != ring }) {
                        if (simulate(
                                You(weapon, armor, setOf(ring, ring2)),
                                Boss(input.first, input.second, input.third)
                            )) {
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
        for (weapon in weapons) {
            if (!simulate(
                    You(weapon, null, emptySet()),
                    Boss(input.first, input.second, input.third)
                )) {
                maxGold = max(maxGold, weapon.cost)
            }
            for (ring in rings) {
                if (!simulate(
                        You(weapon, null, setOf(ring)),
                        Boss(input.first, input.second, input.third)
                    )) {
                    maxGold = max(maxGold, weapon.cost + ring.cost)
                }
                for (ring2 in rings.filter { it != ring }) {
                    if (!simulate(
                            You(weapon, null, setOf(ring, ring2)),
                            Boss(input.first, input.second, input.third)
                        )) {
                        maxGold = max(maxGold, weapon.cost + ring.cost + ring2.cost)
                    }
                }
            }
            for (armor in armors) {
                if (!simulate(
                        You(weapon, armor, emptySet()),
                        Boss(input.first, input.second, input.third)
                    )) {
                    maxGold = max(maxGold, weapon.cost + armor.cost)
                }
                for (ring in rings) {
                    if (!simulate(
                            You(weapon, armor, setOf(ring)),
                            Boss(input.first, input.second, input.third)
                        )) {
                        maxGold = max(maxGold, weapon.cost + armor.cost + ring.cost)
                    }
                    for (ring2 in rings.filter { it != ring }) {
                        if (!simulate(
                                You(weapon, armor, setOf(ring, ring2)),
                                Boss(input.first, input.second, input.third)
                            )) {
                            maxGold = max(maxGold, weapon.cost + armor.cost + ring.cost + ring2.cost)
                        }
                    }
                }
            }
        }
        return maxGold
    }

    private fun simulate(player: You, boss: Boss): Boolean {
        var playerTurn = true
        while (player.hp > 0 && boss.hp > 0) {
            if (playerTurn) {
                boss.defend(player.attack())
            } else {
                player.defend(boss.attack())
            }
            playerTurn = !playerTurn
        }
        return player.hp > 0
    }

    companion object {
        val weapons = setOf(
            Item(8, 4, 0),
            Item(10, 5, 0),
            Item(25, 6, 0),
            Item(40, 7, 0),
            Item(74, 8, 0)
        )
        val armors = setOf(
            Item(13, 0, 1),
            Item(31, 0, 2),
            Item(53, 0, 3),
            Item(75, 0, 4),
            Item(102, 0, 5)
        )
        val rings = setOf(
            Item(25, 1, 0),
            Item(50, 2, 0),
            Item(100, 3, 0),
            Item(20, 0, 1),
            Item(40, 0, 2),
            Item(80, 0, 3)
        )
    }

    abstract class Player(var hp: Int) {
        abstract fun defend(dmg: Int)
        abstract fun attack(): Int
    }

    class Boss(hp: Int, private val dmg: Int, private val arm: Int): Player(hp) {

        override fun defend(dmg: Int) {
            hp -= max(dmg - arm, 1)
        }

        override fun attack() = dmg
    }

    class You(private val weapon: Item, private val armor: Item?, private val rings: Set<Item>) : Player(100) {

        override fun defend(dmg: Int) {
            hp -= max(dmg - weapon.arm - (armor?.arm ?: 0) - rings.sumBy { it.arm }, 1)
        }

        override fun attack() = weapon.dmg + (armor?.dmg ?: 0) + rings.sumBy { it.dmg }
    }

    data class Item(val cost: Int, val dmg: Int, val arm: Int)
}