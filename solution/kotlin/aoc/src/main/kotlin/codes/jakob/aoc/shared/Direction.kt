package codes.jakob.aoc.shared

enum class SimpleDirection(val expanded: ExpandedDirection) {
    NORTH(ExpandedDirection.NORTH),
    EAST(ExpandedDirection.EAST),
    SOUTH(ExpandedDirection.SOUTH),
    WEST(ExpandedDirection.WEST)
}

enum class ExpandedDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH_EAST,
    SOUTH_WEST
}
