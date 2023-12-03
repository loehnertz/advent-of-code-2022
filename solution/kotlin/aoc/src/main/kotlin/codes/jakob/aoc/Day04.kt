package codes.jakob.aoc

import codes.jakob.aoc.shared.*

@Suppress("SimplifyBooleanWithConstants")
class Day04 : Solution() {
    override fun solvePart1(input: String): Any {
        return parseRanges(input)
            .count { (a, b) -> a fullyContains b || b fullyContains a }
    }

    override fun solvePart2(input: String): Any {
        return parseRanges(input)
            .count { (a, b) -> a overlaps b || b overlaps a }
    }

    private fun parseRanges(input: String): List<Pair<IntRange, IntRange>> {
        return input
            .splitMultiline()
            .map { pair: String ->
                pair.split(",").map { it.parseRange('-') }.toPair()
            }
    }
}

fun main() = Day04().solve()
