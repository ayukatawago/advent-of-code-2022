package day04

import readInput

fun main() {
    fun String.toIntSetPair(): Pair<Set<Int>, Set<Int>>? {
        val splitInput = split(',')
        if (splitInput.size != 2) return null

        val firstInput = splitInput[0].split('-')
        val secondInput = splitInput[1].split('-')

        if (firstInput.size != 2 || secondInput.size != 2) return null

        val firstRange = firstInput[0].toInt()..firstInput[1].toInt()
        val secondRange = secondInput[0].toInt()..secondInput[1].toInt()
        return firstRange.toSet() to secondRange.toSet()
    }

    fun part1(pairList: List<Pair<Set<Int>, Set<Int>>>): Int {
        var score = 0
        pairList.forEach { (firstSet, secondSet) ->
            if (secondSet + firstSet == secondSet || firstSet + secondSet == firstSet) {
                score++
            }
        }
        return score
    }

    fun part2(pairList: List<Pair<Set<Int>, Set<Int>>>): Int {
        var score = 0
        pairList.forEach { (firstSet, secondSet) ->
            if ((secondSet - firstSet) != secondSet || (firstSet - secondSet) != firstSet) {
                score++
            }
        }
        return score
    }


    val testInput = readInput("day04/Day04_test")
    val testPairList: List<Pair<Set<Int>, Set<Int>>> = testInput.mapNotNull { it.toIntSetPair() }

    println(part1(testPairList))
    println(part2(testPairList))
}
