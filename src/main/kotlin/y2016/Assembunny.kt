package y2016

class Assembunny(private val register: MutableMap<String, Int>) {

    fun run(instructions: MutableList<String>, onOutput: (Int) -> Boolean = { true }) {
        var index = 0
        while (index < instructions.size) {
            val split = instructions[index].split(" ")
            val cmd = split[0]
            val arg1 = split[1]
            val arg2 = split.getOrNull(2)
            when (cmd) {
                "cpy" -> {
                    register[arg2!!] = arg1.toIntOrNull() ?: register[arg1]!!
                    index++
                }
                "inc" -> {
                    register[arg1] = register[arg1]!! + 1
                    index++
                }
                "dec" -> {
                    register[arg1] = register[arg1]!! - 1
                    index++
                }
                "jnz" -> {
                    if (arg1.toIntOrNull() ?: register[arg1]!! != 0) index += arg2!!.toIntOrNull() ?: register[arg2]!! else index++
                }
                "tgl" -> {
                    val move = register[arg1]!!
                    if (index + move in instructions.indices) {
                        val tglCmd = instructions[index + move].split(" ")[0]
                        instructions[index + move] = instructions[index + move].replace(tglCmd, tglCmd.toggle())
                    }
                    index++
                }
                "out" -> {
                    if (onOutput.invoke(arg1.toIntOrNull() ?: register[arg1]!!)) index++
                    else return
                }
            }
        }
    }

    private fun String.toggle() = when (this) {
        "inc" -> "dec"
        "dec" -> "inc"
        "jnz" -> "cpy"
        "cpy" -> "jnz"
        "tgl" -> "inc"
        else -> throw RuntimeException("oh no!")
    }
}