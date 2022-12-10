package day06

import readInput

fun main() {
    val testInput = readInput("day06/Day06_test")

    testInput.forEach {
        println(solution(it, 4))
    }
    testInput.forEach {
        println(solution(it, 14))
    }
}

private fun solution(input: String, num: Int): Int {
    if (input.length < num) return 0

    (0 until (input.lastIndex - num)).forEach { index ->
        val charSet = input.slice(index until index + num).toSet()
        if (charSet.size == num) {
            return index + num
        }
    }
    return 0
}