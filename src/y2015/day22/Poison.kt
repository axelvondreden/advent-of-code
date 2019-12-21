package y2015.day22

object Poison: Spell() {
    override var cost = 173
    override fun cast(wizard: Wizard, boss: Boss) {
        wizard.mana -= cost
        wizard.addEffect(Effect("Poison",  6) { w, b, c ->
            run {
                if (c > 0) {
                    b.defend(3)
                }
            }
        })
    }
}