package y2015.day22

object Shield: Spell() {
    override var cost = 113
    override fun cast(wizard: Wizard, boss: Boss) {
        wizard.mana -= cost
        wizard.addEffect(Effect("Shield", 6) { w, b, c ->
            run {
                if (c == 6) {
                    w.arm += 7
                } else if (c == 1) {
                    w.arm -= 7
                }
            }
        })
    }
}