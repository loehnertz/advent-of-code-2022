package codes.jakob.aoc

import codes.jakob.aoc.shared.map
import codes.jakob.aoc.shared.splitMultiline
import codes.jakob.aoc.shared.toPair
import com.google.gson.Gson


class Day13 : Solution() {
    private val gson = Gson()

    override fun solvePart1(input: String): Any {
        val pairs = input
            .split("\n\n")
            .map { it.splitMultiline().toPair().map { packet -> parsePacket(packet) } }
            .mapIndexed { index, pair -> index + 1 to isInRightOrder(pair) }
            .filter { it.second }
            .map { it.first }
        return 0
    }

    private fun isInRightOrder(pair: Pair<Any, Any>): Boolean {
        fun recurse(left: Any, right: Any): Boolean {
            return when (left) {
                is Double -> {
                    when (right) {
                        is Double -> left < right
                        is List<*> -> right.find { recurse(left, it as Any) } != null
                        else -> error("")
                    }
                }

                is List<*> -> {
                    when (right) {
                        is Double -> recurse(left.first() as Any, right)
                        is List<*> -> recurse(left.first() as Any, right.first() as Any)
                        else -> error("")
                    }
                }

                else -> error("")
            }
        }
        return recurse(pair.first, pair.second)
    }

    override fun solvePart2(input: String): Any {
        TODO("Not yet implemented")
    }

    private fun parsePacket(toParse: String): Any {
        return gson.fromJson(toParse, Any::class.java)
    }

    data class Packet(
        val content: List<Any>
    ) {
        data class Node(
            val content: List<Int>
        )
    }
}

fun main() = Day13().solve()
