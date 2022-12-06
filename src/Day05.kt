import java.util.Stack

private data class Action(val amount: Int, val from: Int, val to: Int) {
    companion object {
        fun from(input: String): Action? {
            val regex = """move (\d*) from (\d*) to (\d*)""".toRegex()
            val result = regex.matchEntire(input) ?: return null
            return Action(result.groupValues[1].toInt(), result.groupValues[2].toInt(), result.groupValues[3].toInt())
        }
    }
}

fun main() {
    fun part1(testInput: List<String>): String {
        val stackSize =
            testInput.firstOrNull { it.startsWith(" 1") }?.filter { !it.isWhitespace() }?.length ?: return ""
        val stackArray = Array<Stack<Char>>(stackSize) { Stack() }

        val conditions = testInput.filter { it.trim().startsWith('[') }
        conditions.reversed().forEach { condition ->
            (0 until stackSize).forEach { index ->
                if (condition.length > index * 4 + 1) {
                    val char = condition[index * 4 + 1]
                    if (!char.isWhitespace()) {
                        stackArray[index].push(char)
                    }
                }
            }
        }
        val actions = testInput.filter { it.startsWith("move") }.mapNotNull { Action.from(it) }

        actions.forEach { action ->
            var count = 0
            while (count < action.amount) {
                stackArray[action.to - 1].push(stackArray[action.from - 1].pop())
                count++
            }
        }

        return stackArray.map { it.pop() }.joinToString("")
    }

    fun part2(testInput: List<String>): String {
        val stackSize =
            testInput.firstOrNull { it.startsWith(" 1") }?.filter { !it.isWhitespace() }?.length ?: return ""
        val stackArray = Array<Stack<Char>>(stackSize) { Stack() }

        val conditions = testInput.filter { it.trim().startsWith('[') }
        conditions.reversed().forEach { condition ->
            (0 until stackSize).forEach { index ->
                if (condition.length > index * 4 + 1) {
                    val char = condition[index * 4 + 1]
                    if (!char.isWhitespace()) {
                        stackArray[index].push(char)
                    }
                }
            }
        }
        val actions = testInput.filter { it.startsWith("move") }.mapNotNull { Action.from(it) }

        actions.forEach { action ->
            val holder = mutableListOf<Char>()

            var count = 0
            while (count < action.amount) {
                holder.add(stackArray[action.from - 1].pop())
                count++
            }
            holder.reversed().forEach {
                stackArray[action.to - 1].push(it)
            }
        }

        return stackArray.map { it.pop() }.joinToString("")
    }

    val testInput = readInput("Day05_test")

    println(part1(testInput))
    println(part2(testInput))
}
