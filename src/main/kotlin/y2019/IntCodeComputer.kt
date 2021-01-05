package y2019

class IntCodeComputer(private var register: LongArray, private val outputZeroes: Boolean = true, private val haltAfterInput: Boolean = false) {

    private var pointer = 0
    private var inputs = longArrayOf()
    private var inputCounter = 0
    private var relativeBase = 0
    private var inputLambda: (() -> Long)? = null

    fun withInputs(inputs: LongArray): IntCodeComputer {
        this.inputs = inputs
        return this
    }

    fun addInput(input: Long): IntCodeComputer {
        inputs = inputs.plus(input)
        return this
    }

    fun withInputFunction(f: () -> Long): IntCodeComputer {
        inputLambda = f
        return this
    }

    fun run(): Reply {
        register = register.copyOf(99999)
        while (true) {
            var op = register[pointer]
            val modes = intArrayOf(0, 0, 0)
            if (op >= 100) {
                op = op.toString().takeLast(2).toLong()
                var index = 2
                while (index < register[pointer].toString().length) {
                    modes[index - 2] = register[pointer].toString().reversed()[index].toString().toInt()
                    index++
                }
            }
            when (op.toInt()) {
                1 -> add(modes)
                2 -> multiply(modes)
                3 -> input(modes)
                4 -> {
                    pointer += 2
                    if (modes[0] == 0) {
                        if (register[register[pointer - 1].toInt()] > 0 || outputZeroes) {
                            return Reply(register, register[register[pointer - 1].toInt()])
                        }
                    } else if (modes[0] == 1) {
                        if (register[pointer - 1] > 0 || outputZeroes) {
                            return Reply(register, register[pointer - 1])
                        }
                    } else {
                        if (register[relativeBase + register[pointer - 1].toInt()] > 0 || outputZeroes) {
                            return Reply(register, register[relativeBase + register[pointer - 1].toInt()])
                        }
                    }
                }
                5 -> jumpIfNotZero(modes)
                6 -> jumpIfZero(modes)
                7 -> lessThan(modes)
                8 -> equal(modes)
                9 -> adjustRelativeBase(modes)
                99 -> return Reply(register, halted = true, hasOutput = false)
            }
            if (haltAfterInput && op.toInt() == 3) {
                return Reply(register, hasOutput = false)
            }
        }
    }

    private fun getValue(mode: Int, index: Int): Long {
        return when (mode) {
            0 -> register[register[index].toInt()]
            1 -> register[index]
            2 -> register[relativeBase + register[index].toInt()]
            else -> throw Error("unknown mode $mode")
        }
    }

    private fun setValue(mode: Int, index: Int, value: Long) {
        when (mode) {
            0 -> register[register[index].toInt()] = value
            1 -> register[index] = value
            2 -> register[relativeBase + register[index].toInt()] = value
        }
    }

    private fun adjustRelativeBase(modes: IntArray) {
        relativeBase += getValue(modes[0], pointer + 1).toInt()
        pointer += 2
    }

    private fun equal(modes: IntArray) {
        val p1 = getValue(modes[0], pointer + 1)
        val p2 = getValue(modes[1], pointer + 2)
        setValue(modes[2], pointer + 3, if (p1 == p2) 1 else 0)
        pointer += 4
    }

    private fun lessThan(modes: IntArray) {
        val p1 = getValue(modes[0], pointer + 1)
        val p2 = getValue(modes[1], pointer + 2)
        setValue(modes[2], pointer + 3, if (p1 < p2) 1 else 0)
        pointer += 4
    }

    private fun jumpIfZero(modes: IntArray) {
        if (getValue(modes[0], pointer + 1) == 0L) {
            pointer = getValue(modes[1], pointer + 2).toInt()
        } else {
            pointer += 3
        }
    }

    private fun jumpIfNotZero(modes: IntArray) {
        if (getValue(modes[0], pointer + 1) != 0L) {
            pointer = getValue(modes[1], pointer + 2).toInt()
        } else {
            pointer += 3
        }
    }

    private fun input(modes: IntArray) {
        setValue(modes[0], pointer + 1, if (inputLambda != null) inputLambda!!.invoke() else inputs[inputCounter])
        inputCounter++
        pointer += 2
    }

    private fun multiply(modes: IntArray) {
        val result = getValue(modes[0], pointer + 1) * getValue(modes[1], pointer + 2)
        setValue(modes[2], pointer + 3, result)
        pointer += 4
    }

    private fun add(modes: IntArray) {
        val result = getValue(modes[0], pointer + 1) + getValue(modes[1], pointer + 2)
        setValue(modes[2], pointer + 3, result)
        pointer += 4
    }

    data class Reply(val register: LongArray, val value: Long = 0L, val halted: Boolean = false, val hasOutput: Boolean = true) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Reply
            if (!register.contentEquals(other.register)) return false
            if (value != other.value) return false
            if (halted != other.halted) return false
            if (hasOutput != other.hasOutput) return false
            return true
        }

        override fun hashCode(): Int {
            var result = register.contentHashCode()
            result = 31 * result + value.hashCode()
            result = 31 * result + halted.hashCode()
            result = 31 * result + hasOutput.hashCode()
            return result
        }
    }
}