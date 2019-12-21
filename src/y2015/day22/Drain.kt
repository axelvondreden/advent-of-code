package y2015.day22

object Drain: Spell() {
    override var cost = 73
    override fun cast(wizard: Wizard, boss: Boss) {
        wizard.mana -= cost
        wizard.hp += 2
        boss.defend(2)
    }
}