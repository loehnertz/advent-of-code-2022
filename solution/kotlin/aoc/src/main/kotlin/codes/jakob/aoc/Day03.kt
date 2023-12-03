package codes.jakob.aoc

import codes.jakob.aoc.shared.splitByEach
import codes.jakob.aoc.shared.splitInHalf
import codes.jakob.aoc.shared.splitMultiline

class Day03 : Solution() {
    override fun solvePart1(input: String): Any {
        return input
            .splitMultiline()
            .asSequence()
            .map { it.splitByEach() }
            .map { it.splitInHalf() }
            .map { (compartmentA: List<Char>, compartmentB: List<Char>) ->
                compartmentA.toSet() intersect compartmentB.toSet()
            }
            .map { it.first() }
            .sumOf { it.toPriority() }
    }

    override fun solvePart2(input: String): Any {
        return input
            .splitMultiline()
            .asSequence()
            .chunked(3)
            .map { group: List<String> ->
                group.map { rucksack: String -> rucksack.splitByEach().toSet() }
            }
            .map { (elfA: Set<Char>, elfB: Set<Char>, elfC: Set<Char>) ->
                elfA intersect elfB intersect elfC
            }
            .map { it.first() }
            .sumOf { it.toPriority() }
    }
}

fun Char.toPriority(): Int {
    return if (this.isUpperCase()) {
        (this - 38).code
    } else {
        (this - 96).code
    }
}

fun main() = Day03().solve()
