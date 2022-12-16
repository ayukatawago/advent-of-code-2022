package day14

import readInput

fun main() {
    val testInput = readInput("day14/test")

    val rocks = mutableSetOf<Position>()
    testInput.forEach { input ->
        val iterator =
            input.split(" -> ").map { Position(it.split(',')[1].toInt(), it.split(',')[0].toInt()) }.iterator()
        var currentPosition = iterator.next()
        while (iterator.hasNext()) {
            val nextPosition = iterator.next()
            if (currentPosition.row == nextPosition.row) {
                getIntRange(currentPosition.column, nextPosition.column).forEach { column ->
                    rocks.add(Position(currentPosition.row, column))
                }
            } else if (currentPosition.column == nextPosition.column) {
                getIntRange(currentPosition.row, nextPosition.row).forEach { row ->
                    rocks.add(Position(row, currentPosition.column))
                }
            }
            currentPosition = nextPosition
        }
    }
    part1(rocks)
    part2(rocks)
}
private fun part1(rocks: Set<Position>) {
    val sands = mutableSetOf<Position>()
    var sandPosition = Position.START
    var shouldRun = true
    while (shouldRun) {
        val nextPosition = listOf(
            Position(sandPosition.row + 1, sandPosition.column),
            Position(sandPosition.row + 1, sandPosition.column - 1),
            Position(sandPosition.row + 1, sandPosition.column + 1)
        ).firstOrNull { it !in rocks && it !in sands }
        when {
            nextPosition == null && sandPosition == Position.START -> {
                sands.add(Position.START)
                shouldRun = false
            }
            nextPosition == null -> {
                sands.add(sandPosition)
                sandPosition = Position.START
            }
            nextPosition.row == rocks.maxOf { it.row } -> shouldRun = false
            else -> sandPosition = nextPosition
        }
    }
    printStatus(rocks, sands, Position.START)
    println(sands.size)
}

private fun part2(rocks: Set<Position>) {
    val floorRow = rocks.maxOf { it.row + 2 }
    val floors = mutableSetOf<Position>()
    (Position.START.column - floorRow..Position.START.column + floorRow).forEach { colomn ->
        floors.add(Position(floorRow, colomn))
    }
    part1(rocks + floors)
}

private fun printStatus(rocks: Set<Position>, sands: Set<Position>, startPosition: Position) {
    val left = rocks.minOf { it.column - 1 }
    val right = rocks.maxOf { it.column + 1 }
    val bottom = rocks.maxOf { it.row + 1 }

    (0..bottom).forEach { row ->
        (left..right).forEach { column ->
            when {
                Position(row, column) in rocks -> print('#')
                Position(row, column) in sands -> print('o')
                row == startPosition.row && column == startPosition.column -> print('+')
                else -> print('.')
            }
        }
        println()
    }
}

private fun getIntRange(num1: Int, num2: Int): IntRange =
    if (num1 >= num2) {
        num2..num1
    } else {
        num1..num2
    }

private data class Position(val row: Int, val column: Int) {
    companion object {
        val START = Position(0, 500)
    }
}