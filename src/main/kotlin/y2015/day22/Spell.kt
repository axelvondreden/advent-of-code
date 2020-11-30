package y2015.day22

abstract class Spell {
    abstract val cost: Int

    abstract fun cast(wizard: Wizard, boss: Boss)
}