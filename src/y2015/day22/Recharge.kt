package y2015.day22

object Recharge: Spell() {
    override var cost = 229
    override fun cast(wizard: Wizard, boss: Boss) {
        wizard.mana -= cost
        wizard.addEffect(Effect("Recharge", 5) { w, b, c ->
            run {
                if (c > 0) {
                    w.mana += 101
                }
            }
        })
    }
}