package day20

import readInput

fun main() {
    val input = readInput("day20/test")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Long {
    return solve(input)
}

private fun part2(input: List<String>): Long {
    return solve(input, 811589153, 10)
}

private fun solve(input: List<String>, decryptionKey: Int = 1, round: Int = 1): Long {
    val numbers = input.mapIndexed { index, s -> Number(index, s.toLong() * decryptionKey) }
    val size = numbers.size
    val mutableList = numbers.toMutableList()
    repeat(round) {
        mutableList.indices.forEach { originalIndex ->
            val index = mutableList.indexOfFirst { originalIndex == it.originalIndex }
            val number = mutableList.removeAt(index)
            val nextIndex = (index + number.value).mod(size - 1)
            mutableList.add(nextIndex, number)
        }
    }
    println(mutableList)
    val zeroIndex = mutableList.indexOfFirst { it.value == 0L }
    return listOf(1000, 2000, 3000).sumOf {
        println(mutableList[(zeroIndex + it).mod(size)])
        mutableList[(zeroIndex + it).mod(size)].value
    }
}

private data class Number(val originalIndex: Int, val value: Long)