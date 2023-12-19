package y2016

import Day
import utils.md5

class Day05 : Day<String>(2016, 5) {

    override suspend fun List<String>.parse() = first()

    override suspend fun solve1(input: String): String {
        var password = ""
        var i = 0
        while (password.length < 8) {
            val md5 = "$input$i".md5()
            if (md5.startsWith("00000")) password += md5[5]
            i++
        }
        return password
    }

    override suspend fun solve2(input: String): String {
        val password = CharArray(8) { ' ' }
        var i = 0
        while (password.contains(' ')) {
            val md5 = "$input$i".md5()
            if (md5.startsWith("00000")) {
                val pos = md5[5].toString().toIntOrNull()
                if (pos != null && pos in 0..7 && password[pos] == ' ') password[pos] = md5[6]
            }
            i++
        }
        return password.joinToString("")
    }
}