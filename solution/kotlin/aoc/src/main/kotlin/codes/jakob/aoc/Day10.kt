package codes.jakob.aoc

import codes.jakob.aoc.Day10.Instruction.Type.ADD_X
import codes.jakob.aoc.Day10.Instruction.Type.NOOP
import codes.jakob.aoc.shared.splitMultiline

class Day10 : Solution() {
    override fun solvePart1(input: String): Any {
        var cycle = 1
        var x = 1
        val instructions: List<Instruction> = input.splitMultiline().map { parseInstruction(it) }
        val recorded: MutableList<Pair<Int, Int>> = mutableListOf()
        for (instruction in instructions) {
            when (instruction.type) {
                NOOP -> {
                    recorded += cycle to x
                    cycle++
                }

                ADD_X -> {
                    repeat(2) {
                        recorded += cycle to x
                        cycle++
                    }
                    x += instruction.value!!
                }
            }
        }
        return recorded.filter { it.first in signalCycles }.sumOf { it.first * it.second }
    }

    override fun solvePart2(input: String): Any {
        TODO("Not yet implemented")
    }

    private fun parseInstruction(line: String): Instruction {
        return if (line == "noop") {
            Instruction(NOOP, null)
        } else {
            val (type, value) = line.split(" ")
            when (type) {
                "addx" -> Instruction(ADD_X, value.toInt())
                else -> error("Unknown instruction type: $type")
            }
        }
    }

    data class Instruction(
        val type: Type,
        val value: Int?,
    ) {
        enum class Type {
            NOOP,
            ADD_X
        }
    }

    companion object {
        private val signalCycles: Set<Int> = setOf(20, 60, 100, 140, 180, 220)
    }
}

fun main() = Day10().solve()
