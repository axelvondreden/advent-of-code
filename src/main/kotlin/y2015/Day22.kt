package y2015

import Day
import y2015.day22.*

class Day22 : Day() {

    override val input = Pair(58, 9)

    override fun solve1(): Int {
        val v = intArrayOf(999999)
        getMinSpellCost(
            Wizard(),
            Boss(input.first, input.second), v, 0, false)
        return v[0] - 20
    }

    override fun solve2(): Int {
        val v = intArrayOf(999999)
        getMinSpellCost(
            Wizard(),
            Boss(input.first, input.second), v, 0, true)
        return v[0]
    }

    private fun getMinSpellCost(wizard: Wizard, boss: Boss, v: IntArray, cost: Int, part2: Boolean) {
        var myCost = cost
        try {
            wizard.turn(boss)

            val spells = getAvailableSpells(wizard)
            if (spells.isEmpty()) {
                return
            }
            for (s in spells) {
                val w = wizard.copy()
                val b = boss.copy()
                myCost = cost + s.cost
                s.cast(w, b)

                if (part2) {
                    w.hp--
                }

                w.turn(b)

                w.defend(b.dmg)
                getMinSpellCost(w, b, v, myCost, part2)
            }
        } catch (e: IllegalStateException) {
            if (e.message == "Wizard dead") {
                return
            } else if (e.message == "Boss dead") {
                if (myCost < v[0]) {
                    v[0] = myCost
                }
                return
            }
            throw e
        }
    }

    private fun getAvailableSpells(wizard: Wizard): List<Spell> {
        val list = mutableListOf<Spell>()
        if (wizard.mana >= Drain.cost) {
            list.add(Drain)
        }
        if (wizard.mana >= MagicMissile.cost) {
            list.add(MagicMissile)
        }
        if (wizard.mana >= Poison.cost && wizard.activeEffects.none { it.name == "Poison" }) {
            list.add(Poison)
        }
        if (wizard.mana >= Recharge.cost && wizard.activeEffects.none { it.name == "Recharge" }) {
            list.add(Recharge)
        }
        if (wizard.mana >= Shield.cost && wizard.activeEffects.none { it.name == "Shield" }) {
            list.add(Shield)
        }
        return list
    }
}