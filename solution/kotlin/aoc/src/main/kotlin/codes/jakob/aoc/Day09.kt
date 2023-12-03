package codes.jakob.aoc

import codes.jakob.aoc.shared.Coordinates
import codes.jakob.aoc.shared.ExpandedDirection
import codes.jakob.aoc.shared.ExpandedDirection.*
import codes.jakob.aoc.shared.splitMultiline
import codes.jakob.aoc.shared.toPair

class Day09 : Solution() {
    override fun solvePart1(input: String): Any {
        return simulateVisitedTailCoordinates(parseInstructions(input), 1).count()
    }

    override fun solvePart2(input: String): Any {
        return simulateVisitedTailCoordinates(parseInstructions(input), 9).count()
    }

    private fun simulateVisitedTailCoordinates(instructions: List<Instruction>, knotAmount: Int): Set<Coordinates> {
        var headCoordinates = Coordinates(0, 0)
        val allKnotCoordinates: MutableList<Coordinates> = MutableList(knotAmount) { headCoordinates }
        val visitedTailCoordinates: MutableSet<Coordinates> = mutableSetOf(allKnotCoordinates.last())

        for (instruction: Instruction in instructions) {
            repeat(instruction.distance) {
                headCoordinates = headCoordinates.inDirection(instruction.direction)

                for ((index: Int, knotCoordinates: Coordinates) in allKnotCoordinates.withIndex()) {
                    val inFront: Coordinates = if (index == 0) {
                        headCoordinates
                    } else {
                        allKnotCoordinates[index - 1]
                    }

                    if (knotCoordinates distanceToDiagonally inFront > 1) {
                        val inWhichDirection: ExpandedDirection = knotCoordinates.inWhichDirection(inFront)!!
                        allKnotCoordinates[index] = knotCoordinates.inDirection(inWhichDirection)
                        if (index == allKnotCoordinates.size - 1) visitedTailCoordinates += allKnotCoordinates[index]
                    }
                }
            }
        }

        return visitedTailCoordinates
    }

    private fun parseInstructions(input: String): List<Instruction> {
        return input
            .splitMultiline()
            .map { it.split(" ").toPair() }
            .map { (a, b) -> Instruction(parseDirection(a), b.toInt()) }
    }

    private fun parseDirection(directionalLetter: String): ExpandedDirection {
        return when (directionalLetter) {
            "U" -> NORTH
            "R" -> EAST
            "D" -> SOUTH
            "L" -> WEST
            else -> error("Direction unknown")
        }
    }

    data class Instruction(
        val direction: ExpandedDirection,
        val distance: Int,
    )
}

fun main() = Day09().solve()
