package y2015.day22

object MagicMissile: Spell() {
    override var cost = 53
    override fun cast(wizard: Wizard, boss: Boss) {
        wizard.mana -= cost
        boss.defend(4)
    }
}