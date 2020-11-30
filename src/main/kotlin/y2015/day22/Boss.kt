package y2015.day22

import kotlin.math.max

class Boss(private var hp: Int, val dmg: Int) {

    fun defend(dmg: Int) {
        hp -= max(dmg, 1)
        if (hp <= 0) {
            error("Boss dead")
        }
    }

    fun copy() = Boss(hp, dmg)
}