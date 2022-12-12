package day11

import readInput

fun main() {
    val testInput = readInput("day11/test")

    part1(testInput)
    part2(testInput)
}

private fun part1(testInput: List<String>) {
    val solution = Solution.from(testInput)
    solution.calculate1()
}

private fun part2(testInput: List<String>) {
    val solution = Solution.from(testInput)
    solution.calculate2()
}

private class Solution {
    val monkeys = mutableListOf<Monkey>()

    fun addNewMonkey(initialItems: List<Long>, operation: (Long) -> Long, test: Int, monkey1: Int, monkey2: Int) {
        val monkey = Monkey(operation, test, monkey1 to monkey2).apply {
            addItems(initialItems)
        }
        monkeys.add(monkey)
    }

    fun Iterable<Long>.lcm(): Long = reduce { a, b -> a * b }

    fun calculate1() {
        repeat(20) {
            monkeys.forEach {
                it.inspect1()
            }
        }
        val (first, second) = monkeys.map { it.inspectCount }.sortedDescending().take(2)
        println(first.toLong() * second.toLong())
    }

    fun calculate2() {
        val modulus = monkeys.map { it.divisible.toLong() }.lcm()
        repeat(10000) {
            monkeys.forEach {
                it.inspect2(modulus)
            }
        }
        val (first, second) = monkeys.map { it.inspectCount }.sortedDescending().take(2)
        println(first.toLong() * second.toLong())
    }

    inner class Monkey(
        val operation: (Long) -> Long,
        val divisible: Int,
        val nextMonkey: Pair<Int, Int>
    ) {
        val items: MutableList<Long> = mutableListOf()

        var inspectCount = 0

        fun addItems(newItems: List<Long>) {
            items.addAll(newItems)
        }

        fun inspect1() {
            while (items.isNotEmpty()) {
                val item = items.removeFirst()
                val newLevel = operation(item) / 3
                if (newLevel % divisible == 0L) {
                    monkeys[nextMonkey.first].items.add(newLevel)
                } else {
                    monkeys[nextMonkey.second].items.add(newLevel)
                }
                inspectCount++
            }
        }

        fun inspect2(modulus: Long) {
            while (items.isNotEmpty()) {
                val item = items.removeFirst()
                val newLevel = operation(item) % modulus
                if (newLevel % divisible == 0L) {
                    monkeys[nextMonkey.first].items.add(newLevel)
                } else {
                    monkeys[nextMonkey.second].items.add(newLevel)
                }
                inspectCount++
            }
        }
    }

    companion object {
        private fun createOperation(operation: String): (Long) -> Long {
            val (_, operator, value) = operation.split(" ")
            return when (operator) {
                "+" -> { old -> old + value.toInt() }
                "*" -> { old ->
                    if (value == "old") {
                        old * old
                    } else {
                        old * value.toInt()
                    }
                }
                else -> { old -> old }
            }
        }

        fun from(testInput: List<String>): Solution {
            val solution = Solution()
            var index = 0
            while (index < testInput.size) {
                val itemsLine = testInput[index + 1]
                val operationLine = testInput[index + 2]
                val test = testInput[index + 3].filter { it.isDigit() }.toInt()
                val trueMonkey = testInput[index + 4].filter { it.isDigit() }.toInt()
                val falseMonkey = testInput[index + 5].filter { it.isDigit() }.toInt()
                val items = itemsLine.replace("  Starting items: ", "").split(", ")
                val operation = createOperation(operationLine.replace("  Operation: new = ", ""))

                solution.addNewMonkey(items.map { it.toLong() }, operation, test, trueMonkey, falseMonkey)
                index += 7
            }

            return solution
        }
    }
}