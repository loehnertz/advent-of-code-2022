package codes.jakob.aoc

import codes.jakob.aoc.ClockCircuit.Instruction.Type.ADD_X
import codes.jakob.aoc.ClockCircuit.Instruction.Type.NOOP
import codes.jakob.aoc.ClockCircuit.Register
import codes.jakob.aoc.shared.splitMultiline
import java.util.*

class Day10_fucked : Solution() {
    override fun solvePart1(input: String): Any {
        val instructions: List<ClockCircuit.Instruction> = input.splitMultiline().map { parseInstruction(it) }
        val command: (Int, RegisterState) -> Pair<Int, Long> = { cycle: Int, registerState: RegisterState ->
            cycle to cycle * registerState.getValue(Register.X)
        }
        val recorded: Set<Pair<Int, Long>> = ClockCircuit(instructions, listOf(command)).run()
        return recorded.filter { it.first in signalCycles }.sumOf { it.second }
    }

    override fun solvePart2(input: String): Any {
        TODO()
    }

    private fun parseInstruction(line: String): ClockCircuit.Instruction {
        return if (line == "noop") {
            ClockCircuit.Instruction(NOOP, null)
        } else {
            val (type, value) = line.split(" ")
            when (type) {
                "addx" -> ClockCircuit.Instruction(ADD_X, value.toInt())
                else -> error("Unknown instruction type: $type")
            }
        }
    }

    companion object {
        private val signalCycles: Set<Int> = setOf(20, 60, 100, 140, 180, 220)
    }
}

class ClockCircuit<T>(
    private val instructions: List<Instruction>,
    private val recordCommands: List<(Int, RegisterState) -> T>,
) {
    fun run(): Set<T> {
        var cycle = 0

        val registerState: RegisterState = mutableMapOf<Register, Long>().withDefault { 1 }

        val recorded: MutableList<T> = mutableListOf()

        val eventQueue: PriorityQueue<Event> = PriorityQueue(instructions.convertToEvents())
        while (eventQueue.isNotEmpty()) {
            val event: Event = eventQueue.poll()

            val residue: Residue = event.residue()
            residue.effects.forEach { it(registerState) }
            eventQueue.addAll(residue.events)

            recorded.addAll(recordCommands.mapNotNull { it(cycle, registerState) })
            
            cycle = event.cycle
        }

        return recorded.toSet()
    }

    class TriggerAdd(cycle: Int, private val register: Register, private val value: Long) : Event(cycle) {
        override fun residue(): Residue {
            return Residue(
                events = listOf(ApplyAdd(cycle + 2, register, value)),
            )
        }
    }

    class ApplyAdd(cycle: Int, private val register: Register, private val value: Long) : Event(cycle) {
        override fun residue(): Residue {
            return Residue(
                effects = listOf { state -> state[register] = state.getValue(register) + value },
            )
        }
    }

    class Noop(cycle: Int) : Event(cycle) {
        override fun residue(): Residue = Residue()
    }

    abstract class Event(val cycle: Int) : Comparable<Event> {
        abstract fun residue(): Residue

        override fun compareTo(other: Event): Int = this.cycle.compareTo(other.cycle)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Event) return false

            if (cycle != other.cycle) return false

            return true
        }

        override fun hashCode(): Int {
            return cycle
        }

        override fun toString(): String {
            return "TickEvent(cycle=$cycle)"
        }
    }

    data class Residue(
        val effects: Collection<(RegisterState) -> Unit> = emptyList(),
        val events: Collection<Event> = emptyList(),
    )

    enum class Register {
        X
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

    private fun Collection<Instruction>.convertToEvents(): Collection<Event> {
        return this.mapIndexed { index: Int, instruction: Instruction ->
            val cycle: Int = index + 1
            when (instruction.type) {
                NOOP -> Noop(cycle)
                ADD_X -> TriggerAdd(cycle, Register.X, instruction.value!!.toLong())
            }
        }
    }
}

typealias RegisterState = MutableMap<Register, Long>

fun main() = Day10().solve()
