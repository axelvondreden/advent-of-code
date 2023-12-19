package y2019

import Day
import java.math.BigInteger
import kotlin.math.abs

class Day22 : Day<List<String>>(2019, 22) {

    override suspend fun List<String>.parse() = this

    override suspend fun solve1(input: List<String>) = solve(input, 2019, 10007, 1)

    override suspend fun solve2(input: List<String>) = solve(input, 2020, 119315717514047, -101741582076661)

    private fun solve(input: List<String>, x: Long, n: Long, k: Long): Long {
        var a = BigInteger.ONE
        var b = BigInteger.ZERO
        val m = n.toBigInteger()

        for (ln in input) {
            when {
                ln == "deal into new stack" -> {
                    a = (-a).mod(m)
                    b = b.not().mod(m)
                }
                "cut" in ln -> {
                    val i = ln.split(' ').last().toBigInteger()
                    b = (b - i).mod(m)
                }
                "deal with increment" in ln -> {
                    val i = ln.split(' ').last().toBigInteger()
                    a = a * i % m
                    b = b * i % m
                }
                else -> error("Unrecognized instruction: $ln")
            }
        }
        if (k < 0) {
            a = a.modInverse(m)
            b = (-b * a).mod(m)
        }

        var c = BigInteger.ONE
        var d = BigInteger.ZERO
        var e = abs(k)

        while (e > 0) {
            if (e and 1 == 1L) {
                c = a * c % m
                d = (a * d + b) % m
            }
            e = e shr 1
            b = (a * b + b) % m
            a = a * a % m
        }

        return (x.toBigInteger() * c + d).mod(m).toLong()
    }

    fun doShuffle(input: List<String>, n: Int, k: Int): IntArray {
        val deck = IntArray(n) { it }
        repeat(k) {
            input.forEach { ln ->
                when {
                    ln == "deal into new stack" -> deck.reverse()
                    "cut" in ln -> {
                        val arg = ln.split(' ').last().toInt()
                        val new = IntArray(n) { deck[it + arg umod n] }
                        new.copyInto(deck)
                    }
                    "deal with increment" in ln -> {
                        val arg = ln.split(' ').last().toInt()
                        val new = IntArray(n)
                        var i = 0
                        repeat(n) { j ->
                            new[i] = deck[j]
                            i += arg
                            i %= n
                        }
                        new.copyInto(deck)
                    }
                    else -> error("Unrecognized instruction: $ln")
                }
            }
        }
        return deck
    }

    private infix fun Int.umod(base: Int) = Math.floorMod(this, base)
}