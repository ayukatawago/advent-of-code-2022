package day17

import readInput

fun main() {
    val testInput = readInput("day17/test")
    val windPattern = testInput[0].map {
        when (it) {
            '<' -> -1
            '>' -> 1
            else -> error("Invalid input")
        }
    }
    println(solve(windPattern, 2022))
    println(solve(windPattern, 1000000000000))
}

private const val INITIAL_X = 2
private const val WIDTH = 7
private const val START_HEIGHT = 4
private fun solve(windPattern: List<Int>, totalRockCount: Long): Long {
    var windIndex = 0
    val cave: MutableSet<Position> = (0..6).map { Position(it, 0) }.toMutableSet()
    val stateMap: HashMap<State, Pair<Long, Int>> = hashMapOf()

    var loops = 0L
    var loopHeight = 0
    var rockCount = 0L
    var isLoopFound = false
    while (rockCount < totalRockCount) {
        val rockIndex = (rockCount % RockShape.values().size).toInt()
        val rockShape = RockShape.values()[rockIndex]
        var rockPosition = Position(INITIAL_X, cave.maxY() + START_HEIGHT)
        while (rockShape.canFall(cave, rockPosition)) {
            val direction = windPattern[windIndex]
            windIndex = (windIndex + 1) % windPattern.size

            if (rockShape.canShift(cave, direction, rockPosition)) {
                rockPosition = Position(rockPosition.x + direction, rockPosition.y)
            }
            rockPosition = Position(rockPosition.x, rockPosition.y - 1)
        }
        rockShape.rocks.forEach {
            cave.add(Position(it.x + rockPosition.x, it.y + rockPosition.y + 1))
        }

        val newState = State(cave.getTopShape(), rockIndex, windIndex)
        if (stateMap.containsKey(newState) && !isLoopFound) {
            val (loopStart, loopBaseHeight) = stateMap[newState]!!
            println("$rockCount -> Hit ($newState) $loopStart $loopBaseHeight")
            val rocksPerLoop = rockCount - loopStart
            println("rocksPerLoop = $rocksPerLoop")
            loops = (totalRockCount - loopStart) / rocksPerLoop - 1
            loopHeight = cave.maxY() - loopBaseHeight
            rockCount += rocksPerLoop * (loops - 1) + 1
            isLoopFound = true
        } else {
            stateMap[newState] = rockCount to cave.maxY()
            rockCount++
        }
    }
    // cave.render()
    return cave.maxY() + loopHeight * (loops - 1)
}

private data class State(val caveSnapshot: List<Int>, val rockIndex: Int, val windIndex: Int)

private fun Set<Position>.maxY(): Int = maxOf { it.y }

private fun Set<Position>.getTopShape(): List<Int> =
    groupBy { it.x }.entries.sortedBy { it.key }.map { it.value.maxBy { point -> point.y } }
        .let {
            it.map { point -> maxY() - point.y }
        }

private fun Set<Position>.render() {
    groupBy { it.y }.entries.sortedByDescending { it.key }.map { it.value }.forEach { positions ->
        (0..6).forEach { x ->
            print(if (x in positions.map { it.x }) '#' else '.')
        }
        println()
    }
}

private enum class RockShape(val rocks: List<Position>, val width: Int, val height: Int) {
    SHAPE1(listOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(3, 0)), 4, 1),
    SHAPE2(listOf(Position(1, 0), Position(0, 1), Position(1, 1), Position(2, 1), Position(1, 2)), 3, 3),
    SHAPE3(listOf(Position(0, 0), Position(1, 0), Position(2, 0), Position(2, 1), Position(2, 2)), 3, 3),
    SHAPE4(listOf(Position(0, 0), Position(0, 1), Position(0, 2), Position(0, 3)), 1, 4),
    SHAPE5(listOf(Position(0, 0), Position(1, 0), Position(0, 1), Position(1, 1)), 2, 2);

    fun canFall(cave: Set<Position>, position: Position): Boolean =
        rocks.all {
            !cave.contains(Position(it.x + position.x, it.y + position.y))
        }

    fun canShift(cave: Set<Position>, direction: Int, position: Position): Boolean =
        rocks.all {
            position.x + direction >= 0 &&
                    position.x + width + direction <= WIDTH &&
                    !cave.contains(Position(it.x + position.x + direction, it.y + position.y))
        }
}

private data class Position(val x: Int, val y: Int)
