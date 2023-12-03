package codes.jakob.aoc.shared

fun String.splitMultiline(): List<String> = split("\n")

inline fun <T, R> Pair<T, T>.map(block: (T) -> R): Pair<R, R> {
    return this.let { (first: T, second: T) ->
        block(first) to block(second)
    }
}

inline fun <A, B, C, D> Pair<A, B>.map(blockA: (A) -> C, blockB: (B) -> D): Pair<C, D> {
    return this.let { (first: A, second: B) ->
        blockA(first) to blockB(second)
    }
}

fun CharSequence.splitByEach(): List<Char> = split("").drop(1).dropLast(1).map { it.first() }

fun <E> List<E>.splitInHalf(): Pair<List<E>, List<E>> {
    return this.subList(0, this.size / 2) to this.subList(this.size / 2, this.size)
}

fun List<Int>.binaryToDecimal(): Int {
    require(this.all { it == 0 || it == 1 }) { "Expected bit string, but received $this" }
    return Integer.parseInt(this.joinToString(""), 2)
}

fun Int.bitFlip(): Int {
    require(this == 0 || this == 1) { "Expected bit, but received $this" }
    return this.xor(1)
}

fun String.toBitString(): List<Int> {
    val bits: List<String> = split("").filter { it.isNotBlank() }
    require(bits.all { it == "0" || it == "1" }) { "Expected bit string, but received $this" }
    return bits.map { it.toInt() }
}

/**
 * [Transposes](https://en.wikipedia.org/wiki/Transpose) the given list of nested lists (a matrix, in essence).
 *
 * This function is adapted from this [post](https://stackoverflow.com/a/66401340).
 */
fun <T> List<List<T>>.transpose(): List<List<T>> {
    val result: MutableList<MutableList<T>> = (this.first().indices).map { mutableListOf<T>() }.toMutableList()
    this.forEach { columns -> result.zip(columns).forEach { (rows, cell) -> rows.add(cell) } }
    return result
}

infix fun <T : Comparable<T>> ClosedRange<T>.fullyContains(other: ClosedRange<T>): Boolean {
    return this.start in other && this.endInclusive in other
}

infix fun <T : Comparable<T>> ClosedRange<T>.overlaps(other: ClosedRange<T>): Boolean {
    return this.start in other || this.endInclusive in other
}

fun <E> List<E>.toPair(): Pair<E, E> {
    require(this.size == 2) { "The given list has to contain exactly two elements, instead found ${this.size}" }
    return this[0] to this[1]
}

fun String.parseRange(delimiter: Char = '-'): IntRange {
    val (from: Int, to: Int) = this.split(delimiter).map { it.toInt() }
    return from..to
}

@Synchronized
fun <K, V> MutableMap<K, V>.getOrCompute(key: K, valueFunction: () -> V): V {
    return this[key] ?: valueFunction().also { this[key] = it }
}

fun <E> List<E>.splitEvery(n: Int): List<E> {
    val drop: Sequence<Int> = generateSequence(1) { it + 1 }.map { (3 + 1) * it - 1 }.take(this.size)
    return this.mapIndexedNotNull { index, e -> if (index in drop) null else e }
}

fun <T, R> Collection<T>.allUnique(block: (T) -> R): Boolean {
    val mapped: List<R> = this.map(block)
    return mapped.size == mapped.toSet().size
}

fun <E> Collection<E>.cartesianProduct(): List<Pair<E, E>> {
    return this.flatMap { lhs: E -> this.map { rhs: E -> lhs to rhs } }
}

fun Iterable<Int>.multiply(): Int = reduce { acc, int -> acc * int }

fun Char.parseInt(): Int = toString().toInt()

fun <T> String.parseMatrix(block: (Char) -> T): List<List<T>> {
    return this.splitMultiline().map { it.splitByEach().map(block) }
}

fun <T> String.parseGrid(block: (Char) -> T): Grid<T> {
    return Grid.fromMatrix(this.parseMatrix(block))
}
