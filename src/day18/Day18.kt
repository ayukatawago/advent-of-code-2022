package day18

import readInput

fun main() {
    val testInput = readInput("day18/test")
    val cubes = testInput.map { it.split(',') }.map { Point3D(it[0].toInt(), it[1].toInt(), it[2].toInt()) }

    println(part1(cubes))
    println(part2(cubes))
}

private fun part1(cubes: List<Point3D>): Int =
    cubes.sumOf { cube ->
        cube.neighbors.count { it !in cubes }
    }

private fun part2(cubes: List<Point3D>): Int {
    val xRange = cubes.minOf { it.x } - 1..cubes.maxOf { it.x } + 1
    val yRange = cubes.minOf { it.y } - 1..cubes.maxOf { it.y } + 1
    val zRange = cubes.minOf { it.z } - 1..cubes.maxOf { it.z } + 1
    val checkedPoints = mutableSetOf<Point3D>()
    var count = 0
    val queue = mutableListOf<Point3D>()
    queue.add(Point3D(xRange.first, yRange.first, zRange.first))
    while (queue.isNotEmpty()) {
        val point = queue.removeFirst()
        if (point in checkedPoints) continue

        checkedPoints.add(point)
        point.neighbors
            .filter { it.x in xRange && it.y in yRange && it.z in zRange }
            .forEach {
                if (it in cubes) {
                    count++
                } else {
                    queue.add(it)
                }
            }
    }
    return count
}

private data class Point3D(val x: Int, val y: Int, val z: Int) {
    val neighbors: Set<Point3D>
        get() = setOf(
            Point3D(x - 1, y, z),
            Point3D(x + 1, y, z),
            Point3D(x, y - 1, z),
            Point3D(x, y + 1, z),
            Point3D(x, y, z - 1),
            Point3D(x, y, z + 1)
        )
}