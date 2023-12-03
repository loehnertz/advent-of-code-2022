package codes.jakob.aoc

import codes.jakob.aoc.shared.Grid
import codes.jakob.aoc.shared.parseGrid
import java.util.*
import java.util.Comparator.comparing

class Day12 : Solution() {
    override fun solvePart1(input: String): Any {
        val grid: Grid<Elevation> = input.parseGrid { Elevation(it) }
        val startCell: Grid.Cell<Elevation> = grid.cells.first { it.content.value.isStart }

        val validPaths: MutableSet<List<Grid.Cell<Elevation>>> = mutableSetOf()

        val queue: PriorityQueue<List<Grid.Cell<Elevation>>> = PriorityQueue(comparing { it.count() })
        queue.add(listOf(startCell))
        while (queue.isNotEmpty()) {
            val path: List<Grid.Cell<Elevation>> = queue.poll()
            val lastHop: Grid.Cell<Elevation> = path.last()
            val lastElevation: Elevation = lastHop.content.value
            if (lastElevation.isEnd) {
                validPaths += path
            } else {
                lastHop
                    .getAdjacent()
                    .filterNot { it in path }
                    .filter {
                        val heightDelta = it.content.value.height() - lastElevation.height()
                        heightDelta == 0 || heightDelta == 1 || lastElevation.isStart
                    }
                    .forEach { queue.add(path + it) }
            }
        }

        return validPaths.minOf { it.count() } - 1
    }

    override fun solvePart2(input: String): Any {
        TODO("Not yet implemented")
    }

    data class Elevation(
        val letter: Char,
        val isStart: Boolean = letter == 'S',
        val isEnd: Boolean = letter == 'E',
    ) {
        fun height(): Int {
            return when (letter) {
                'S' -> 'a'.code
                'E' -> 'z'.code
                else -> letter.code
            }
        }
    }
}

fun main() = Day12().solve()
