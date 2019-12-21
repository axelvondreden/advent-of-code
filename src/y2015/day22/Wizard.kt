package y2015.day22

import kotlin.math.max

class Wizard {

    var hp = 50
    var mana = 500
    var arm = 0
    var activeEffects = mutableListOf<Effect>()

    fun addEffect(effect: Effect) {
        activeEffects.add(effect)
    }

    fun defend(dmg: Int) {
        hp -= max(dmg - arm, 1)
        if (hp <= 0) {
            error("Wizard dead")
        }
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