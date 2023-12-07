package y2020

import Day

class Day04 : Day<List<String>>(2020, 4) {

    override val input = readStrings()

    private val passportStrings = parsePassportStrings()

    private val passports = parsePassports()

    override fun solve1(input: List<String>) = passports.size

    override fun solve2(input: List<String>) = passports.count { it.hasValidValues() }

    private fun parsePassportStrings(): MutableSet<String> {
        var lineNr = 0
        var currentPassportLine = " "
        val list = mutableSetOf<String>()
        while (lineNr < input.size) {
            if (input[lineNr].isBlank()) {
                list.add(currentPassportLine)
                currentPassportLine = " "
            } else {
                currentPassportLine += " " + input[lineNr]
            }
            lineNr++
        }
        if (currentPassportLine.isNotBlank()) {
            list.add(currentPassportLine)
        }
        return list
    }

    private fun parsePassports() = passportStrings.filter { it.hasPassportFields() }.map { line ->
        val map = line.trim().split(Regex("\\s+")).associate { it.split(":")[0] to it.split(":")[1] }
        Passport(map["byr"]!!, map["iyr"]!!, map["eyr"]!!, map["hgt"]!!, map["hcl"]!!, map["ecl"]!!, map["pid"]!!)
    }

    private fun String.hasPassportFields() = fields.all { contains(" $it:") }

    private companion object {
        val fields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    }

    private data class Passport(val byr: String, val iyr: String, val eyr: String, val hgt: String, val hcl: String, val ecl: String, val pid: String) {
        fun hasValidValues(): Boolean {
            if ((byr.toIntOrNull() ?: 0) !in 1920..2002) return false
            if ((iyr.toIntOrNull() ?: 0) !in 2010..2020) return false
            if ((eyr.toIntOrNull() ?: 0) !in 2020..2030) return false
            if (hgt.endsWith("cm") || hgt.endsWith("in")) {
                val hgtValue = hgt.dropLast(2).toIntOrNull() ?: return false
                if (hgt.endsWith("cm") && hgtValue !in 150..193) return false
                if (hgt.endsWith("in") && hgtValue !in 59..76) return false
            } else return false
            if (hcl.first() == '#' && hcl.length == 7) {
                if (hcl.drop(1).any { it !in '0'..'9' && it !in 'a'..'f' }) return false
            } else return false
            if (ecl !in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")) return false
            if (pid.length != 9 || pid.any { it !in '0'..'9' }) return false
            return true
        }
    }
}