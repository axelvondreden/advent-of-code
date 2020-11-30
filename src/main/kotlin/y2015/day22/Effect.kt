package y2015.day22

class Effect(var name: String, var left: Int, private val effect: (Wizard, Boss, Int) -> Unit) {

    fun trigger(w: Wizard, b: Boss) {
        effect.invoke(w, b, left)
        left--
    }

    fun copy() = Effect(name, left, this.effect)
}