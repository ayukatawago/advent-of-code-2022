package day08

import readInput
import kotlin.math.min

fun main() {
    fun part1(grid: List<List<Int>>): Int {
        val rowSize = grid.size
        val columnSize = grid[0].size
        val rowRange = (0 until rowSize)
        val columnRange = (0 until columnSize)

        val visibleMap = Array(rowSize) { BooleanArray(columnSize) { false } }

        columnRange.forEach { column ->
            visibleMap[0][column] = true
            visibleMap[visibleMap.lastIndex][column] = true
        }
        rowRange.forEach { row ->
            visibleMap[row][0] = true
            visibleMap[row][visibleMap[0].lastIndex] = true
        }

        rowRange.drop(1).dropLast(1).forEach { row ->
            // From left
            var maxLeftHeight = grid[row][0]
            columnRange.drop(1).dropLast(1).forEach { column ->
                if (grid[row][column] > maxLeftHeight) {
                    visibleMap[row][column] = true
                    maxLeftHeight = grid[row][column]
                }
            }

            // From right
            var maxRightHeight = grid[row][columnSize - 1]
            columnRange.drop(1).dropLast(1).reversed().forEach { column ->
                if (grid[row][column] > maxRightHeight) {
                    visibleMap[row][column] = true
                    maxRightHeight = grid[row][column]
                }
            }
        }

        columnRange.drop(1).dropLast(1).forEach { column ->
            // From top
            var maxTopHeight = grid[0][column]
            rowRange.drop(1).dropLast(1).forEach { row ->
                if (grid[row][column] > maxTopHeight) {
                    visibleMap[row][column] = true
                    maxTopHeight = grid[row][column]
                }
            }

            // From bottom
            var maxBottomHeight = grid[rowSize - 1][column]
            rowRange.drop(1).dropLast(1).reversed().forEach { row ->
                if (grid[row][column] > maxBottomHeight) {
                    visibleMap[row][column] = true
                    maxBottomHeight = grid[row][column]
                }
            }
        }

        return visibleMap.sumOf { it.count { it } }
    }


    fun countVisibleTrees(height: Int, trees: List<Int>): Int {
        var treeIndex = 0
        while (treeIndex <= trees.lastIndex && height > trees[treeIndex]) {
            treeIndex++
        }

        return min(treeIndex, trees.lastIndex) + 1
    }

    fun part2(grid: List<List<Int>>): Int {
        var maxScenicScore = 0

        val rowSize = grid.size
        val columnSize = grid[0].size
        val rowRange = (0 until rowSize)
        val columnRange = (0 until columnSize)

        rowRange.drop(1).dropLast(1).forEach { row ->
            columnRange.drop(1).dropLast(1).forEach { column ->
                val columns = grid.map { it[column] }
                val leftTrees = grid[row].slice(0 until column)
                val rightTrees = grid[row].slice(column + 1 until columnSize)
                val topTrees = columns.slice(0 until row)
                val bottomTrees = columns.slice(row + 1 until rowSize)
                val leftScore = countVisibleTrees(grid[row][column], leftTrees.reversed())
                val rightScore = countVisibleTrees(grid[row][column], rightTrees)
                val topScore = countVisibleTrees(grid[row][column], topTrees.reversed())
                val bottomScore = countVisibleTrees(grid[row][column], bottomTrees)
                val scenicScore = leftScore * rightScore * topScore * bottomScore
                if (maxScenicScore < scenicScore) {
                    maxScenicScore = scenicScore
                }
            }
        }

        return maxScenicScore
    }

    val testInput = readInput("day08/Day08_test")

    val grid = testInput.map { it.map { it.code - '0'.code } }
    println(part1(grid))
    println(part2(grid))
}
