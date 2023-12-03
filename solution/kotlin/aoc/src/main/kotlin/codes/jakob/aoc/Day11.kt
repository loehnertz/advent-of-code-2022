package codes.jakob.aoc

import codes.jakob.aoc.shared.multiply
import codes.jakob.aoc.shared.parseInt
import codes.jakob.aoc.shared.splitMultiline

class Day11 : Solution() {
    override fun solvePart1(input: String): Any {
        val monkeys: List<Monkey> = input.split("\n\n").map { parseMonkey(it) }
        val worryDecrease: (Long) -> Long = { level -> level / 3 }
        return calculateMonkeyBusinessLevel(monkeys, 20, worryDecrease)
    }

    override fun solvePart2(input: String): Any {
        val monkeys: List<Monkey> = input.split("\n\n").map { parseMonkey(it) }
        val lowestCommonMultiple: Int = monkeys.map { it.test.divideBy }.multiply()
        val worryDecrease: (Long) -> Long = { level -> level % lowestCommonMultiple }
        return calculateMonkeyBusinessLevel(monkeys, 10000, worryDecrease)
    }

    private fun calculateMonkeyBusinessLevel(
        monkeys: Collection<Monkey>,
        rounds: Int,
        worryDecrease: (Long) -> Long,
    ): Long {
        val monkeysById: Map<Int, Monkey> = monkeys.associateBy { it.id }

        val monkeyInspections: MutableMap<Monkey, Long> = mutableMapOf<Monkey, Long>().withDefault { 0 }
        repeat(rounds) {
            for (monkey: Monkey in monkeysById.values) {
                for (item: Long in monkey.items.toList()) {
                    // Inspect
                    val newItem: Long = worryDecrease(monkey.operation(item))
                    monkeyInspections[monkey] = monkeyInspections.getValue(monkey) + 1

                    // Test
                    val nextMonkey: Monkey = if (monkey.test.check(newItem)) {
                        monkeysById.getValue(monkey.test.ifTrue)
                    } else {
                        monkeysById.getValue(monkey.test.ifFalse)
                    }

                    // Pass on
                    monkey.items.remove(item).also { nextMonkey.items.add(newItem) }
                }
            }
        }

        return monkeyInspections.values.sortedDescending().take(2).multiply()
    }

    private fun parseMonkey(toParse: String): Monkey {
        val lines: List<String> = toParse.splitMultiline()

        val id: Int = lines[0].substringAfter(" ").first().parseInt()
        val items: List<Long> = lines[1].substringAfter(": ").split(", ").map { it.toLong() }
        val operation: (Long) -> Long = parseOperation(lines[2].substringAfter("old "))
        val test: Test = parseTest(lines[3].substringAfter(": "), lines[4], lines[5])

        return Monkey(id, items.toMutableList(), operation, test)
    }

    private fun parseOperation(toParse: String): (Long) -> Long {
        val (sign, number) = toParse.split(" ")
        return when (sign) {
            "+" -> if (number == "old") { old -> old + old } else { old -> old + number.toInt() }
            "-" -> if (number == "old") { old -> old - old } else { old -> old - number.toInt() }
            "*" -> if (number == "old") { old -> old * old } else { old -> old * number.toInt() }
            "/" -> if (number == "old") { old -> old / old } else { old -> old / number.toInt() }
            else -> error("Unknown sign: $sign")
        }
    }

    private fun parseTest(toParse: String, ifTrueMonkey: String, ifFalseMonkey: String): Test {
        val divisibleBy: Int = toParse.substringAfterLast(" ").toInt()
        return Test(
            { level -> level % divisibleBy == 0L },
            divisibleBy,
            ifTrueMonkey.last().parseInt(),
            ifFalseMonkey.last().parseInt(),
        )
    }

    data class Monkey(
        val id: Int,
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val test: Test,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Monkey) return false

            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            return id
        }

        override fun toString(): String {
            return "Monkey(id=$id)"
        }
    }

    data class Test(
        val check: (Long) -> Boolean,
        val divideBy: Int,
        val ifTrue: Int,
        val ifFalse: Int,
    )
}

fun Collection<Long>.multiply(): Long = reduce { acc, l -> acc * l }

fun main() = Day11().solve()
