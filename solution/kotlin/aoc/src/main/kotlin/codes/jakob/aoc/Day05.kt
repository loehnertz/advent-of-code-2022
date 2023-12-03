package codes.jakob.aoc

import codes.jakob.aoc.shared.*
import java.util.*

class Day05 : Solution() {
    override fun solvePart1(input: String): Any {
        val (stacksById: Map<CrateStack, Stack<Crate>>, instructions: List<Instruction>) = parseInput(input)

        for (instruction: Instruction in instructions) {
            val from: Stack<Crate> = stacksById[instruction.from]!!
            val to: Stack<Crate> = stacksById[instruction.to]!!
            repeat(instruction.amount) { to.add(from.pop()) }
        }

        return stacksById.values.map { it.pop().id }.joinToString("")
    }

    override fun solvePart2(input: String): Any {
        val (stacksById: Map<CrateStack, Stack<Crate>>, instructions: List<Instruction>) = parseInput(input)

        for (instruction: Instruction in instructions) {
            val from: Stack<Crate> = stacksById[instruction.from]!!
            val to: Stack<Crate> = stacksById[instruction.to]!!
            (0 until instruction.amount).map { from.pop() }.asReversed().forEach(to::add)
        }

        return stacksById.values.map { it.pop().id }.joinToString("")
    }

    private fun parseInput(input: String): Pair<Map<CrateStack, Stack<Crate>>, List<Instruction>> {
        return input
            .split("\n\n")
            .toPair()
            .map(this::parseStacks, this::parseInstructions)
    }

    private fun parseStacks(state: String): Map<CrateStack, Stack<Crate>> {
        return state
            .splitMultiline()
            .map { it.splitByEach().splitEvery(3) }
            .transpose()
            .filter { it.last().isDigit() }
            .groupBy { CrateStack(it.last().toString().toInt()) }
            .mapValues { (_, crates) ->
                crates
                    .first()
                    .filterNot { it.isWhitespace() }
                    .dropLast(1)
                    .map { Crate(it) }
            }
            .mapValues { (_, crates) ->
                Stack<Crate>().also { crates.asReversed().forEach(it::add) }
            }
    }

    private fun parseInstructions(instructions: String): List<Instruction> {
        return instructions
            .splitMultiline()
            .mapNotNull { INSTRUCTION_REGEX.find(it)?.groupValues }
            .map { match ->
                Instruction(
                    match[1].toInt(),
                    CrateStack(match[2].toInt()),
                    CrateStack(match[3].toInt()),
                )
            }
    }

    data class Instruction(
        val amount: Int,
        val from: CrateStack,
        val to: CrateStack,
    )

    @JvmInline
    value class CrateStack(
        val id: Int
    )

    @JvmInline
    value class Crate(
        val id: Char
    )

    companion object {
        private val INSTRUCTION_REGEX: Regex = Regex("move\\s(\\d+)\\sfrom\\s(\\d+)\\sto\\s(\\d+)")
    }
}

fun main() = Day05().solve()
