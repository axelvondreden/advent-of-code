package y2015

import Day
import kotlin.math.max
import kotlin.math.min

class Day22 : Day<Pair<Int, Int>>(2015, 22) {

    data class Stats(
        val hp: Int,
        val armor: Int,
        val power: Int
    )

    data class Spell(
        val cost: Int,
        val duration: Int,
        val action: (Pair<Stats, Stats>) -> Pair<Stats, Stats>
    )

    private val magicMissile = Spell(53, 1) { (me, boss) ->
        me to boss.copy(hp = boss.hp - 4)
    }
    private val drain = Spell(73, 1) { (me, boss) ->
        me.copy(hp = me.hp + 2) to boss.copy(hp = boss.hp - 2)
    }
    private val shield = Spell(113, 6) { (me, boss) ->
        me.copy(armor = 7) to boss
    }
    private val poison = Spell(173, 6) { (me, boss) ->
        me to boss.copy(hp = boss.hp - 3)
    }
    private val recharge = Spell(229, 5) { (me, boss) ->
        me.copy(power = me.power + 101) to boss
    }

    private val bossHit = Spell(0, 1) { (me, boss) ->
        me.copy(hp = me.hp - max(boss.power - me.armor, 1)) to boss
    }
    private val hardBossHit = Spell(0, 1) { (me, boss) ->
        me.copy(hp = me.hp - max(boss.power - me.armor, 1) - 1) to boss
    }

    private val spells = listOf(magicMissile, drain, shield, poison, recharge)

    data class Game(
        val me: Stats,
        val boss: Stats,
        val spells: Map<Spell, Int> = mapOf(),
        val mana: Int = 0,
        val myMove: Boolean = true
    )

    private fun countMana(
        bossHp: Int,
        bossPower: Int,
        mySpells: List<Spell>,
        bossSpells: List<Spell>
    ): Int {
        val me = Stats(50, 0, 500)
        val boss = Stats(bossHp, 0, bossPower)
        val queue = ArrayDeque(listOf(Game(me, boss)))
        var best = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val game = queue.removeFirst()

            val states = game.spells.keys
                .fold(game.me to game.boss) { acc, spell -> spell.action(acc) }
                .let { state ->
                    if (game.spells[shield] != 1) {
                        state
                    } else {
                        state.copy(first = state.first.copy(armor = 0))
                    }
                }

            if (states.first.hp <= 0) {
                continue
            }
            if (states.second.hp <= 0) {
                best = min(best, game.mana)
                continue
            }
            val activeSpells = game.spells
                .mapValues { it.value - 1 }
                .filterValues { it > 0 }

            val spells = if (game.myMove) {
                mySpells
                    .filter { it.cost <= states.first.power }
                    .filterNot { it in activeSpells }
                    .filter { game.mana + it.cost < best }
            } else {
                bossSpells
            }

            queue += spells.map { spell ->
                game.copy(
                    me = states.first.copy(power = states.first.power - spell.cost),
                    boss = states.second,
                    spells = activeSpells + (spell to spell.duration),
                    mana = game.mana + spell.cost,
                    myMove = !game.myMove,
                )
            }
        }
        return best
    }

    override fun List<String>.parse() = with(map { it.split(": ")[1].toInt() }) { get(0) to get(1)}

    override fun solve1(input: Pair<Int, Int>): Int {
        return countMana(input.first, input.second, spells, listOf(bossHit))
    }

    override fun solve2(input: Pair<Int, Int>): Int {
        return countMana(input.first, input.second, spells, listOf(hardBossHit))
    }
}