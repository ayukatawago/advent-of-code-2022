package day15

import readInput
import kotlin.math.abs

fun main() {
    val testInput = readInput("day15/test")
    val regex = """Sensor at x=(-*\d+), y=(-*\d+): closest beacon is at x=(-*\d+), y=(-*\d+)""".toRegex()

    val inputs = testInput.mapNotNull {
        val result = regex.matchEntire(it) ?: return@mapNotNull null
        val sensorX = result.groups[1]?.value?.toInt() ?: return@mapNotNull null
        val sensorY = result.groups[2]?.value?.toInt() ?: return@mapNotNull null
        val beaconX = result.groups[3]?.value?.toInt() ?: return@mapNotNull null
        val beaconY = result.groups[4]?.value?.toInt() ?: return@mapNotNull null
        SensorPair(Position(sensorX, sensorY), Position(beaconX, beaconY))
    }
    //part1(inputs, 10)
    //part2(inputs, 20)
    part1(inputs, 2000000)
    part2(inputs, 4000000)
}

private fun part1(inputs: List<SensorPair>, targetY: Int) {
    val positionSet = inputs.mapNotNull { pair ->
        val width = pair.distance - abs(pair.sensor.y - targetY)
        if (width < 0) return@mapNotNull null
        IntRange(pair.sensor.x - width, pair.sensor.x + width).toSet()
    }.reduce { prev, next ->
        prev + next
    }
    println(positionSet.size)
}

private fun part2(inputs: List<SensorPair>, maxRange: Int) {
    val answer = inputs.firstNotNullOf { pair ->
        val topToRightList =
            (0..pair.distance).scan(Position(pair.sensor.x, pair.sensor.y - pair.distance - 1)) { last, _ ->
                Position(last.x + 1, last.y + 1)
            }
        val rightToBottomList =
            (0..pair.distance).scan(Position(pair.sensor.x + pair.distance + 1, pair.sensor.y)) { last, _ ->
                Position(last.x - 1, last.y + 1)
            }
        val bottomToLeftList =
            (0..pair.distance).scan(Position(pair.sensor.x, pair.sensor.y + pair.distance + 1)) { last, _ ->
                Position(last.x - 1, last.y - 1)
            }
        val leftToTopList =
            (0..pair.distance).scan(Position(pair.sensor.x - pair.distance - 1, pair.sensor.y)) { last, _ ->
                Position(last.x + 1, last.y - 1)
            }
        val target = topToRightList + rightToBottomList + bottomToLeftList + leftToTopList

        target.filter { it.x in 0..maxRange && it.y in 0..maxRange }
            .firstOrNull { pos ->
                inputs.none { it.isInRange(pos) }
            }
    }
    println(answer.x * 4000000L + answer.y)
}


private fun printStatus(sensorArea: Set<Position>, beaconArea: Set<Position>, noSensorArea: Set<Position>) {
    val left = minOf(sensorArea.minOf { it.x }, beaconArea.minOf { it.x }, noSensorArea.minOf { it.x })
    val right = maxOf(sensorArea.maxOf { it.x }, beaconArea.maxOf { it.x }, noSensorArea.maxOf { it.x })
    val top = maxOf(sensorArea.maxOf { it.y }, beaconArea.maxOf { it.y }, noSensorArea.maxOf { it.y })
    val bottom = minOf(sensorArea.minOf { it.y }, beaconArea.minOf { it.y }, noSensorArea.minOf { it.y })

    println("$left - $right - $top - $bottom")
    (bottom..top).forEach { row ->
        (left..right).forEach { column ->
            when {
                Position(column, row) in sensorArea -> print('S')
                Position(column, row) in beaconArea -> print('B')
                Position(column, row) in noSensorArea -> print('#')
                else -> print('.')
            }
        }
        println()
    }
    println()
}

private data class Position(val x: Int, val y: Int)

private class SensorPair(val sensor: Position, val beacon: Position) {
    val distance = abs(beacon.x - sensor.x) + abs(beacon.y - sensor.y)

    fun isInRange(position: Position): Boolean {
        return abs(position.x - sensor.x) + abs(position.y - sensor.y) <= distance
    }
}