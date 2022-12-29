package day21

import readInput

fun main() {
    val input = readInput("day21/test")

    val monkeyMap = hashMapOf<String, Monkey>()
    input.forEach {
        val regex = """([a-z]+): ([a-z]+) ([+\-*/]) ([a-z]+)""".toRegex()
        val result = regex.matchEntire(it)?.groupValues ?: return@forEach

        val monkey = Monkey.WaitingMonkey(result[1], result[3][0], result[2], result[4])
        monkeyMap[monkey.name] = monkey
    }
    input.forEach {
        val regex = """([a-z]+): (\d+)""".toRegex()
        val result = regex.matchEntire(it)?.groupValues ?: return@forEach

        val monkey = Monkey.NoWaitMonkey(result[1], result[2].toLong())
        monkeyMap[monkey.name] = monkey
    }
    println(part1(monkeyMap))
    println(part2(monkeyMap))
}

private fun part1(monkeyMap: Map<String, Monkey>): Long {
    return monkeyMap["root"]!!.yell(monkeyMap)
}

private fun part2(monkeyMap: Map<String, Monkey>): Long {
    return monkeyMap["root"]!!.getHumanValue(monkeyMap, 0)
}

sealed class Monkey {
    abstract val name: String
    abstract fun yell(monkeyMap: Map<String, Monkey>): Long

    abstract fun getHumanValue(monkeyMap: Map<String, Monkey>, parentValue: Long): Long

    abstract fun hasHumanChild(monkeyMap: Map<String, Monkey>): Boolean

    data class NoWaitMonkey(override val name: String, private val number: Long) : Monkey() {
        override fun yell(monkeyMap: Map<String, Monkey>): Long = number

        override fun getHumanValue(monkeyMap: Map<String, Monkey>, parentValue: Long): Long =
            if (name == HUMAN_NAME) parentValue else number

        override fun hasHumanChild(monkeyMap: Map<String, Monkey>): Boolean = name == HUMAN_NAME

        companion object {
            private const val HUMAN_NAME = "humn"
        }
    }

    data class WaitingMonkey(
        override val name: String,
        val op: Char,
        val leftMonkeyName: String,
        val rightMonkeyName: String
    ) : Monkey() {
        override fun yell(monkeyMap: Map<String, Monkey>): Long {
            val leftMonkey = monkeyMap[leftMonkeyName]!!
            val rightMonkey = monkeyMap[rightMonkeyName]!!
            return leftMonkey.yell(monkeyMap) op rightMonkey.yell(monkeyMap)
        }

        override fun getHumanValue(monkeyMap: Map<String, Monkey>, parentValue: Long): Long {
            val leftMonkey = monkeyMap[leftMonkeyName]!!
            val rightMonkey = monkeyMap[rightMonkeyName]!!
            return when {
                name == "root" ->
                    if (leftMonkey.hasHumanChild(monkeyMap)) {
                        leftMonkey.getHumanValue(monkeyMap, rightMonkey.yell(monkeyMap))
                    } else {
                        rightMonkey.getHumanValue(monkeyMap, leftMonkey.yell(monkeyMap))
                    }

                leftMonkey.hasHumanChild(monkeyMap) ->
                    leftMonkey.getHumanValue(monkeyMap, parentValue leftInverseOp rightMonkey.yell(monkeyMap))

                else ->
                    rightMonkey.getHumanValue(monkeyMap, parentValue rightInverseOp leftMonkey.yell(monkeyMap))
            }
        }

        override fun hasHumanChild(monkeyMap: Map<String, Monkey>): Boolean {
            val leftMonkey = monkeyMap[leftMonkeyName]!!
            val rightMonkey = monkeyMap[rightMonkeyName]!!
            return leftMonkey.hasHumanChild(monkeyMap) || rightMonkey.hasHumanChild(monkeyMap)
        }

        private infix fun Long.op(right: Long): Long =
            when (op) {
                '+' -> this + right
                '-' -> this - right
                '*' -> this * right
                else -> this / right
            }

        private infix fun Long.leftInverseOp(right: Long): Long =
            when (op) {
                '+' -> this - right
                '-' -> this + right
                '*' -> this / right
                else -> this * right
            }

        private infix fun Long.rightInverseOp(right: Long): Long =
            when (op) {
                '+' -> this - right
                '-' -> right - this
                '*' -> this / right
                else -> right / this
            }
    }
}
