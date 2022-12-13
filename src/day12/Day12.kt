package day12

import readInput

fun main() {
    val testInput = readInput("day12/test")
    val elevationMap = testInput.map { it.map { c -> c.toElevation() } }
    val startRow = testInput.indexOfFirst { it.contains('S') }
    val startColumn = testInput[startRow].indexOfFirst { it == 'S' }
    val endRow = testInput.indexOfFirst { it.contains('E') }
    val endColumn = testInput[endRow].indexOfFirst { it == 'E' }
    part1(elevationMap, startPoint = Point(startRow, startColumn), endPoint = Point(endRow, endColumn))
    part2(elevationMap, startPoint = Point(endRow, endColumn))
}

private fun part1(elevationMap: List<List<Int>>, startPoint: Point, endPoint: Point) {
    println(common(elevationMap, true, startPoint) { point -> point == endPoint })
}

private fun part2(elevationMap: List<List<Int>>, startPoint: Point) {
    println(common(elevationMap, false, startPoint) { point -> elevationMap[point.row][point.column] == 0 })
}

private fun common(
    elevationMap: List<List<Int>>,
    shouldClimb: Boolean,
    startPoint: Point,
    condition: (Point) -> Boolean
): Int {
    val visitedMap = Array(elevationMap.size) { BooleanArray(elevationMap[0].size) { false } }
    visitedMap[startPoint.row][startPoint.column] = true

    val canMove: (Int) -> Boolean = if (shouldClimb) {
        { diff -> diff <= 1 }
    } else {
        { diff -> diff >= -1 }
    }

    var steps = 0
    var visitedPoints = listOf(startPoint)
    while (visitedPoints.isNotEmpty() && visitedPoints.none { condition(it) }) {
        val newVisitedPoints = mutableSetOf<Point>()
        visitedPoints.forEach {
            val currentElevation = elevationMap[it.row][it.column]
            if (it.row + 1 in visitedMap.indices) {
                val diff = elevationMap[it.row + 1][it.column] - currentElevation
                if (canMove(diff) && !visitedMap[it.row + 1][it.column]) {
                    newVisitedPoints.add(Point(it.row + 1, it.column))
                    visitedMap[it.row + 1][it.column] = true
                }
            }
            if (it.row - 1 in visitedMap.indices) {
                val diff = elevationMap[it.row - 1][it.column] - currentElevation
                if (canMove(diff) && !visitedMap[it.row - 1][it.column]) {
                    newVisitedPoints.add(Point(it.row - 1, it.column))
                    visitedMap[it.row - 1][it.column] = true
                }
            }
            if (it.column + 1 in visitedMap[0].indices) {
                val diff = elevationMap[it.row][it.column + 1] - currentElevation
                if (canMove(diff) && !visitedMap[it.row][it.column + 1]) {
                    newVisitedPoints.add(Point(it.row, it.column + 1))
                    visitedMap[it.row][it.column + 1] = true
                }
            }
            if (it.column - 1 in visitedMap[0].indices) {
                val diff = elevationMap[it.row][it.column - 1] - currentElevation
                if (canMove(diff) && !visitedMap[it.row][it.column - 1]) {
                    newVisitedPoints.add(Point(it.row, it.column - 1))
                    visitedMap[it.row][it.column - 1] = true
                }
            }
        }
        steps++
        visitedPoints = newVisitedPoints.toList()
    }
    return steps
}

private data class Point(val row: Int, val column: Int)

private fun Char.toElevation(): Int =
    when (this) {
        'S' -> 0
        'E' -> 'z' - 'a'
        else -> this - 'a'
    }