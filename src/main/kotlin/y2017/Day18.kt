package y2017

import Day

class Day18 : Day(2017, 18) {

    override val input = readStrings().map { it.split(" ") }

    private val register = ('a'..'z').associateWith { 0L }

    override fun solve1(): Long {
        var index = 0
        val reg = register.toMutableMap()
        val sounds = mutableListOf<Long>()
        val recovered = mutableListOf<Long>()
        while (index in input.indices) {
            index += reg.step(input[index], sounds, recovered)
            if (recovered.isNotEmpty()) return recovered.first()
        }
        return 0
    }

    // TODO: make nice
    override fun solve2(): Int {
        var waiting1 = false
        var waiting2 = false
        val queue1 = ArrayDeque<Long>()
        val queue2 = ArrayDeque<Long>()
        var index1 = 0
        var index2 = 0
        val reg1 = register.toMutableMap()
        val reg2 = register.toMutableMap()
        var sentBy1 = 0
        while (!(waiting1 && waiting2)) {
            val sendList = mutableListOf<Long>()
            waiting1 = queue1.isEmpty() && input[index1][0] == "rcv"
            if (!waiting1) {
                index1 += reg1.step(input[index1], sendList, mutableListOf(), true, queue1)
                if (sendList.isNotEmpty()) {
                    queue2.addLast(sendList.removeFirst())
                    sentBy1++
                }
            }
            sendList.clear()
            waiting2 = queue2.isEmpty() && input[index2][0] == "rcv"
            if (!waiting2) {
                index2 += reg2.step(input[index2], sendList, mutableListOf(), true, queue2)
                if (sendList.isNotEmpty()) queue1.addLast(sendList.removeFirst())
            }
        }
        return sentBy1 / 2 // wtf?
    }

    private fun MutableMap<Char, Long>.step(cmd: List<String>, sounds: MutableList<Long>, recovered: MutableList<Long>, mode2: Boolean = false, inputQueue: ArrayDeque<Long> = ArrayDeque()): Int {
        when (cmd[0]) {
            "snd" -> sounds.add(get(cmd[1][0])!!)
            "set" -> put(cmd[1][0], getValue(cmd[2]))
            "add" -> put(cmd[1][0], get(cmd[1][0])!! + getValue(cmd[2]))
            "mul" -> put(cmd[1][0], get(cmd[1][0])!! * getValue(cmd[2]))
            "mod" -> put(cmd[1][0], get(cmd[1][0])!! % getValue(cmd[2]))
            "rcv" -> {
                if (mode2) {
                    put(cmd[1][0], inputQueue.removeFirst())
                } else {
                    if (get(cmd[1][0])!! != 0L) sounds.lastOrNull()?.let { recovered.add(it) }
                }
            }
            "jgz" -> {
                if (getValue(cmd[1]) > 0) return getValue(cmd[2]).toInt()
            }
        }
        return 1
    }

    private fun MutableMap<Char, Long>.getValue(at: String) = at.toLongOrNull() ?: get(at[0])!!
}
