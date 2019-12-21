import java.math.BigInteger
import java.security.MessageDigest


fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun String.hasMinVowels(vowels: Int) = count { "aeiou".contains(it) } >= vowels

fun String.hasDoubleLetter() = (1 until length).any { get(it - 1) == get(it) }

fun String.hasNoBadWords() = arrayOf("ab", "cd", "pq", "xy").none { contains(it) }

fun String.hasLetterPair() = (0 until length - 2).any { substring(it + 2).contains(substring(it..it + 1)) }

fun String.hasDoubleWithOffset() = (0 until length - 2).any { get(it) == get(it + 2) }