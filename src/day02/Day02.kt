package day02

import readInput

private enum class Selection(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    companion object {
        fun from(item: Char): Selection? = when (item) {
            'A' -> ROCK
            'B' -> PAPER
            'C' -> SCISSORS
            else -> null
        }
    }
}

fun main() {
    // X: Rock
    // Y: Paper
    // Z: Scissors
    fun part1(input: List<String>): Int {
        var score = 0
        input.forEach {
            val first = Selection.from(it[0]) ?: return@forEach
            val second = when (it[2]) {
                'X' -> Selection.ROCK
                'Y' -> Selection.PAPER
                'Z' -> Selection.SCISSORS
                else -> null
            } ?: return@forEach
            val result = when {
                first == second -> 3
                second == Selection.ROCK && first == Selection.SCISSORS -> 6
                second.ordinal - first.ordinal == 1 -> 6
                else -> 0
            }
            score += second.score + result
        }

        return score
    }

    // X: Lose
    // Y: Draw
    // Z: Win
    fun part2(input: List<String>): Int {
        var score = 0
        input.forEach {
            val first = Selection.from(it[0]) ?: return@forEach
            val result = when (it[2]) {
                'X' -> 0
                'Y' -> 3
                'Z' -> 6
                else -> null
            } ?: return@forEach
            val second = when (result) {
                0 -> if (first.ordinal == 0) {
                    Selection.SCISSORS
                } else {
                    Selection.values()[first.ordinal - 1]
                }
                3 -> first
                6 -> if (first.ordinal == Selection.values().lastIndex) {
                    Selection.ROCK
                } else {
                    Selection.values()[first.ordinal + 1]
                }
                else -> null
            } ?: return@forEach
            score += second.score + result
        }

        return score
    }

    val testInput = readInput("day02/Day02_test")
    println(part1(testInput))
    println(part2(testInput))
}