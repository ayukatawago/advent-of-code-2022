package day09

import day09.Direction.Companion.toDirection
import readInput
import kotlin.math.abs

fun main() {
    val testInput = readInput("day09/test")
    val motions = testInput.mapNotNull {
        val direction = it.split(" ")[0].toDirection() ?: return@mapNotNull null
        val count = it.split(" ")[1].toInt()
        Motion(direction, count)
    }

    println(solution(motions, 2))
    println(solution(motions, 10))
}

private fun solution(motions: List<Motion>, length: Int): Int {
    val ropePosition = Array(length) { Position(0, 0) }
    val tailPositionSet = mutableSetOf<Position>().also {
        it.add(ropePosition.last())
    }
    motions.forEach { motion ->
        var count = 0
        while (count < motion.step) {
            val headPosition = ropePosition[0]
            ropePosition[0] = when (motion.direction) {
                Direction.UP -> Position(headPosition.x, headPosition.y + 1)
                Direction.DOWN -> Position(headPosition.x, headPosition.y - 1)
                Direction.RIGHT -> Position(headPosition.x + 1, headPosition.y)
                Direction.LEFT -> Position(headPosition.x - 1, headPosition.y)
            }
            var index = 1
            while (index < length) {
                val previousPosition = ropePosition[index - 1]
                val currentPosition = ropePosition[index]
                val diffX = previousPosition.x - currentPosition.x
                val diffY = previousPosition.y - currentPosition.y
                if (abs(diffX) + abs(diffY) > 2) {
                    ropePosition[index] =
                        Position(
                            currentPosition.x + if (diffX > 0) 1 else -1,
                            currentPosition.y + if (diffY > 0) 1 else -1
                        )
                } else {
                    when {
                        abs(diffX) == 2 -> {
                            ropePosition[index] =
                                Position(currentPosition.x + if (diffX > 0) 1 else -1, currentPosition.y)
                        }
                        abs(diffY) == 2 -> {
                            ropePosition[index] =
                                Position(currentPosition.x, currentPosition.y + if (diffY > 0) 1 else -1)
                        }
                        else -> Unit
                    }
                }
                index++
            }
            tailPositionSet.add(ropePosition.last())
            count++
        }
    }
    return tailPositionSet.size
}

private data class Position(val x: Int, val y: Int)

private data class Motion(val direction: Direction, val step: Int)

private enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    companion object {
        fun String.toDirection(): Direction? =
            when (this) {
                "U" -> UP
                "D" -> DOWN
                "L" -> LEFT
                "R" -> RIGHT
                else -> null
            }
    }
}
