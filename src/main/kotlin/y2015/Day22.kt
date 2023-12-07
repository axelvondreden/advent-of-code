package y2015

import Day
import kotlin.math.max

class Day22 : Day<Pair<Int, Int>>(2015, 22) {

    override fun List<String>.parse() = with(map { it.split(": ")[1].toInt() }) { get(0) to get(1)}

    override fun solve1(input: Pair<Int, Int>): Int {
        val v = intArrayOf(999999)
        getMinSpellCost(Wizard(), Boss(input.first, input.second), v, 0, false)
        return v[0] - 20
    }

    override fun solve2(input: Pair<Int, Int>): Int {
        val v = intArrayOf(999999)
        getMinSpellCost(Wizard(), Boss(input.first, input.second), v, 0, true)
        return v[0]
    }

    private fun getMinSpellCost(wizard: Wizard, boss: Boss, v: IntArray, cost: Int, part2: Boolean) {
        var myCost = cost
        try {
            wizard.turn(boss)

            val spells = getAvailableSpells(wizard)
            if (spells.isEmpty()) return
            spells.forEach { s ->
                val w = wizard.copy()
                val b = boss.copy()
                myCost = cost + s.cost
                s.cast(w, b)

                if (part2) w.hp--

                w.turn(b)
                w.defend(b.dmg)
                getMinSpellCost(w, b, v, myCost, part2)
            }
        } catch (e: IllegalStateException) {
            if (e.message == "Wizard dead") return
            else if (e.message == "Boss dead") {
                if (myCost < v[0]) v[0] = myCost
                return
            }
            throw e
        }
    }

    private fun getAvailableSpells(wizard: Wizard): List<Spell> {
        val list = mutableListOf<Spell>()
        if (wizard.mana >= Drain.cost) list.add(Drain)
        if (wizard.mana >= MagicMissile.cost) list.add(MagicMissile)
        if (wizard.mana >= Poison.cost && wizard.activeEffects.none { it.name == "Poison" }) list.add(Poison)
        if (wizard.mana >= Recharge.cost && wizard.activeEffects.none { it.name == "Recharge" }) list.add(Recharge)
        if (wizard.mana >= Shield.cost && wizard.activeEffects.none { it.name == "Shield" }) list.add(Shield)
        return list
    }

    private class Wizard {
        var hp = 50
        var mana = 500
        var arm = 0
        var activeEffects = mutableListOf<Effect>()

        fun addEffect(effect: Effect) {
            activeEffects.add(effect)
        }

        fun defend(dmg: Int) {
            hp -= max(dmg - arm, 1)
            if (hp <= 0) error("Wizard dead")
        }

        fun turn(boss: Boss) {
            activeEffects.forEach { it.trigger(this, boss) }
            activeEffects.removeIf { it.left == 0 }
        }

        fun copy(): Wizard {
            val wizard = Wizard()
            wizard.hp = hp
            wizard.mana = mana
            wizard.arm = arm
            wizard.activeEffects = activeEffects.map { it.copy() }.toMutableList()
            return wizard
        }
    }

    private class Boss(private var hp: Int, val dmg: Int) {

        fun defend(dmg: Int) {
            hp -= max(dmg, 1)
            if (hp <= 0) error("Boss dead")
        }

        fun copy() = Boss(hp, dmg)
    }

    private abstract class Spell {
        abstract val cost: Int

        abstract fun cast(wizard: Wizard, boss: Boss)
    }

    private object Drain : Spell() {
        override var cost = 73
        override fun cast(wizard: Wizard, boss: Boss) {
            wizard.mana -= cost
            wizard.hp += 2
            boss.defend(2)
        }
    }

    private object MagicMissile : Spell() {
        override var cost = 53
        override fun cast(wizard: Wizard, boss: Boss) {
            wizard.mana -= cost
            boss.defend(4)
        }
    }

    private object Poison : Spell() {
        override var cost = 173
        override fun cast(wizard: Wizard, boss: Boss) {
            wizard.mana -= cost
            wizard.addEffect(Effect("Poison", 6) { _, b, c -> if (c > 0) b.defend(3) })
        }
    }

    private object Recharge : Spell() {
        override var cost = 229
        override fun cast(wizard: Wizard, boss: Boss) {
            wizard.mana -= cost
            wizard.addEffect(Effect("Recharge", 5) { w, _, c -> if (c > 0) w.mana += 101 })
        }
    }

    private object Shield : Spell() {
        override var cost = 113
        override fun cast(wizard: Wizard, boss: Boss) {
            wizard.mana -= cost
            wizard.addEffect(Effect("Shield", 6) { w, _, c -> if (c == 6) w.arm += 7 else if (c == 1) w.arm -= 7 })
        }
    }

    private class Effect(var name: String, var left: Int, private val effect: (Wizard, Boss, Int) -> Unit) {

        fun trigger(w: Wizard, b: Boss) {
            effect.invoke(w, b, left)
            left--
        }

        fun copy() = Effect(name, left, this.effect)
    }
}