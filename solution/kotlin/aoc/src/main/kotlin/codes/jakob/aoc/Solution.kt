package codes.jakob.aoc

import java.nio.file.Path
import java.nio.file.Paths

abstract class Solution {
    abstract fun solvePart1(input: String): Any

    abstract fun solvePart2(input: String): Any

    private val identifier: String = getClassName()

    fun solve() {
        println("Solution for part 1: ${solvePart1(retrieveInput())}")
        println("Solution for part 2: ${solvePart2(retrieveInput())}")
    }

    private fun retrieveInput(): String {
        val inputDirectory: Path = Paths.get("").resolve("input").toAbsolutePath()
        return inputDirectory.toFile().resolve("$identifier.$INPUT_FILE_EXTENSION").readText()
    }

    private fun getClassName(): String = this::class.simpleName.toString()

    companion object {
        const val INPUT_FILE_EXTENSION = "txt"
    }
}
