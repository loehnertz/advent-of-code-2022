package codes.jakob.aoc

import codes.jakob.aoc.shared.*

class Day08 : Solution() {
    override fun solvePart1(input: String): Any {
        val grid: Grid<Tree> = input.parseGrid { Tree(it.parseInt()) }
        val visibleTrees: Set<Grid.Cell<Tree>> =
            SimpleDirection.values()
                .flatMap { direction: SimpleDirection ->
                    grid.reduce(
                        direction,
                        { cells: List<Grid.Cell<Tree>> ->
                            var currentMax = -1
                            cells.mapNotNull { cell: Grid.Cell<Tree> ->
                                val tree: Tree = cell.content.value
                                if (tree.height > currentMax) {
                                    currentMax = tree.height
                                    cell
                                } else null
                            }
                        },
                        { it.flatten() },
                    )
                }.toSet()
        return visibleTrees.count()
    }

    override fun solvePart2(input: String): Any {
        val scoreByTree: Grid<Int> = input
            .parseGrid { Tree(it.parseInt()) }
            .map { cell: Grid.Cell<Tree> ->
                SimpleDirection.values()
                    .map { it.expanded }
                    .map { direction: ExpandedDirection ->
                        var keepGoing = true
                        cell.foldInDirection(direction, 0) { visible: Int, other: Grid.Cell<Tree> ->
                            if (keepGoing) {
                                if (cell.content.value.height <= other.content.value.height) keepGoing = false
                                visible + 1
                            } else visible
                        }
                    }.multiply()
            }
        return scoreByTree.cells.maxOfOrNull { it.content.value }!!
    }

    @JvmInline
    value class Tree(
        val height: Int
    )
}

fun main() = Day08().solve()
