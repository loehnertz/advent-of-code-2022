package codes.jakob.aoc

class Day01 : Solution() {
    override fun solvePart1(input: String): Any {
        return calculate(input, 1)
    }

    override fun solvePart2(input: String): Any {
        return calculate(input, 3)
    }

    private fun calculate(input: String, topN: Int): Int {
        return input
            .split("\n\n")
            .asSequence()
            .map { it.split("\n") }
            .map { outer -> outer.sumOf { it.toInt() } }
            .sortedDescending()
            .take(topN)
            .sum()
    }
}

fun main() = Day01().solve()
