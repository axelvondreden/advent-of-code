package y2022

import Day

class Day07 : Day<List<String>>(2022, 7) {

    override val input = readStrings()
    private val rootDir: Directory = Directory("/", null)

    init {
        var cmds = input.drop(2)
        val rootContents = cmds.takeWhile { !it.startsWith("$") }
        rootContents.forEach {
            val s = it.split(" ")
            if (s[0] != "dir") {
                rootDir.files += File(s[1], s[0].toLong())
            }
        }
        cmds = cmds.drop(rootContents.size)
        var currentParent = rootDir
        while (cmds.isNotEmpty()) {
            if (cmds[0] == "$ cd ..") {
                currentParent = currentParent.parent!!
                cmds = cmds.drop(1)
            } else if (cmds[0].startsWith("$ cd")) {
                val d = Directory(cmds[0].split(" ")[2], currentParent)
                currentParent.dirs += d
                currentParent = d
                cmds = cmds.drop(1)
            } else if (cmds[0].startsWith("$ ls")) {
                cmds = cmds.drop(1)
                val contents = cmds.takeWhile { !it.startsWith("$") }
                contents.forEach {
                    val s = it.split(" ")
                    if (s[0] != "dir") {
                        currentParent.files += File(s[1], s[0].toLong())
                    }
                }
                cmds = cmds.drop(contents.size)
            }
        }
    }

    override fun solve1(input: List<String>) = rootDir.getAll().filter { it.size <= 100000 }.sumOf { it.size }

    override fun solve2(input: List<String>) = rootDir.getAll().filter { 70000000 - rootDir.size + it.size >= 30000000 }.minByOrNull { it.size }!!.size

    private class Directory(val name: String, val parent: Directory?) {
        val files = mutableListOf<File>()
        val dirs = mutableListOf<Directory>()

        val size: Long get() = files.sumOf { it.size } + dirs.sumOf { it.size }

        fun getAll(): List<Directory> = dirs.plus(dirs.flatMap { it.getAll() })
    }

    private data class File(val name: String, val size: Long)
}