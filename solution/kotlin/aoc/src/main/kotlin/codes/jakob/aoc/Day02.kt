package codes.jakob.aoc

import codes.jakob.aoc.Day02.Outcome.*
import codes.jakob.aoc.Day02.RockPaperScissors.*
import codes.jakob.aoc.shared.splitMultiline

class Day02 : Solution() {
    override fun solvePart1(input: String): Any {
        return input
            .splitMultiline()
            .map { it.split(" ") }
            .map { RockPaperScissors.fromSign(it[0].first()) to RockPaperScissors.fromSign(it[1].first()) }
            .sumOf { (opponent, me) ->
                me.score + me.play(opponent).points
            }
    }

    override fun solvePart2(input: String): Any {
        return input
            .splitMultiline()
            .map { it.split(" ") }
            .map { RockPaperScissors.fromSign(it[0].first()) to Outcome.fromSign(it[1].first()) }
            .sumOf { (opponent, outcome) ->
                pick(opponent, outcome).score + outcome.points
            }
    }

    private fun pick(opponent: RockPaperScissors, outcome: Outcome): RockPaperScissors {
        return when {
            outcome == DRAW -> opponent
            opponent == ROCK && outcome == WIN -> PAPER
            opponent == ROCK && outcome == LOSE -> SCISSORS
            opponent == PAPER && outcome == WIN -> SCISSORS
            opponent == PAPER && outcome == LOSE -> ROCK
            opponent == SCISSORS && outcome == WIN -> ROCK
            opponent == SCISSORS && outcome == LOSE -> PAPER
            else -> error("Not possible: $opponent + $outcome")
        }
    }

    enum class RockPaperScissors(val score: Int) {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);

        fun play(other: RockPaperScissors): Outcome {
            return when {
                this == other -> DRAW
                this == ROCK && other == PAPER -> LOSE
                this == ROCK && other == SCISSORS -> WIN
                this == PAPER && other == ROCK -> WIN
                this == PAPER && other == SCISSORS -> LOSE
                this == SCISSORS && other == ROCK -> LOSE
                this == SCISSORS && other == PAPER -> WIN
                else -> error("Not possible: $this + $other")
            }
        }

        companion object {
            fun fromSign(sign: Char): RockPaperScissors {
                return when (sign) {
                    'A' -> ROCK
                    'B' -> PAPER
                    'C' -> SCISSORS
                    'X' -> ROCK
                    'Y' -> PAPER
                    'Z' -> SCISSORS
                    else -> error("Not a valid sign: $sign")
                }
            }
        }
    }

    enum class Outcome(val points: Int) {
        WIN(6),
        DRAW(3),
        LOSE(0);

        companion object {
            fun fromSign(sign: Char): Outcome {
                return when (sign) {
                    'X' -> LOSE
                    'Y' -> DRAW
                    'Z' -> WIN
                    else -> error("Not a valid sign: $sign")
                }
            }
        }
    }
}

fun main() = Day02().solve()
