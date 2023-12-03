package codes.jakob.aoc

import codes.jakob.aoc.shared.allUnique
import codes.jakob.aoc.shared.splitByEach

class Day06 : Solution() {
    override fun solvePart1(input: String): Any {
        return findMarkerIndex(input, 4)
    }

    override fun solvePart2(input: String): Any {
        return findMarkerIndex(input, 14)
    }

    private fun findMarkerIndex(input: String, sequenceLength: Int): Int {
        return input
            .splitByEach()
            .asSequence()
            .mapIndexed { index, char -> char to (index + 1) }
            .windowed(sequenceLength)
            .first { sequence -> sequence.allUnique { it.first } }
            .map { it.second }
            .last()
    }
}

fun main() = Day06().solve()
